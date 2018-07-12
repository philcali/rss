package me.philcali.rss.service;

import java.util.Optional;
import java.util.function.Function;

import javax.inject.Inject;

import me.philcali.oauth.api.IAuthManager;
import me.philcali.oauth.api.IClientConfigRepository;
import me.philcali.oauth.api.IExpiringAuthManager;
import me.philcali.oauth.api.INonceRepository;
import me.philcali.oauth.api.ITokenRepository;
import me.philcali.oauth.api.model.IExpiringToken;
import me.philcali.oauth.api.model.IProfile;
import me.philcali.oauth.api.model.IUserClientConfig;
import me.philcali.oauth.spi.OAuthProviders;
import me.philcali.oauth.spi.config.IConfigProvider;
import me.philcali.service.annotations.GET;
import me.philcali.service.annotations.request.PathParam;
import me.philcali.service.annotations.request.QueryParam;
import me.philcali.service.binding.cookie.CookieImpl;
import me.philcali.service.binding.response.IResponse;
import me.philcali.service.binding.response.Response;
import me.philcali.service.binding.response.UnauthorizedException;

public class AuthResource {
    private static final String SESSION_NAME = "srss_id";
    private final INonceRepository nonces;
    private final IClientConfigRepository credentials;
    private final ITokenRepository tokens;
    private final IConfigProvider provider;

    @Inject
    public AuthResource(
            final INonceRepository nonces,
            final IClientConfigRepository credentials,
            final ITokenRepository tokens,
            final IConfigProvider provider) {
        this.nonces = nonces;
        this.credentials = credentials;
        this.tokens = tokens;
        this.provider = provider;
    }

    @GET("/oauth/{type}")
    public String getAuthUrl(@PathParam("type") final String inputType) {
        final IAuthManager manager = OAuthProviders.getAuthManager(inputType, provider, IAuthManager.class);
        return manager.getAuthUrl(nonces.generate(inputType).getId());
    }

    @GET("/oauth/{type}/complete")
    public IResponse completeAuth(
            @PathParam("type") final String inputType,
            @QueryParam("code") final String code,
            @QueryParam("error") final String error,
            @QueryParam("state") final String state) {
        return nonces.verify(state, inputType).map(nonce -> {
            final IExpiringAuthManager login = OAuthProviders.getAuthManager(inputType, IExpiringAuthManager.class);
            return Optional.ofNullable(code)
                    .map(persistToken(login))
                    .map(token -> Response.redirect("/")
                            .withCookies(CookieImpl.builder()
                                    .withHttpOnly(true)
                                    .withName(SESSION_NAME)
                                    .withValue(token.getAccessToken())
                                    .withMaxAge(token.getExpiresIn())
                                    .build())
                            .build())
                    .orElseThrow(() -> new UnauthorizedException(error));
        })
        .orElseThrow(UnauthorizedException::new);
    }

    private Function<String, IExpiringToken> persistToken(final IExpiringAuthManager login) {
        return code -> {
            final IExpiringToken token = login.exchange(code);
            final IProfile profile = login.me(token);
            final IUserClientConfig creds = credentials.listByOwners(profile.getEmail()).getItems().stream()
                    .filter(c -> c.getApi().equals(ServiceAuthorizer.API_TYPE))
                    .findFirst()
                    .orElseGet(() -> credentials.generate(profile.getEmail(), ServiceAuthorizer.API_TYPE));
            return tokens.generate(creds);
        };
    }
}
