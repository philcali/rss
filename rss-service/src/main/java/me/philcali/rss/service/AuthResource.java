package me.philcali.rss.service;

import java.util.Date;
import java.util.Map;
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
import me.philcali.rss.api.IUserSettings;
import me.philcali.rss.api.IUserSettingsRepository;
import me.philcali.rss.api.model.UserSettings;
import me.philcali.service.annotations.GET;
import me.philcali.service.annotations.request.PathParam;
import me.philcali.service.annotations.request.QueryParam;
import me.philcali.service.annotations.response.StatusCode;
import me.philcali.service.binding.cookie.CookieImpl;
import me.philcali.service.binding.response.IResponse;
import me.philcali.service.binding.response.Response;
import me.philcali.service.binding.response.UnauthorizedException;

public class AuthResource {
    private static final String SESSION_NAME = "srss_id";
    private final INonceRepository nonces;
    private final IClientConfigRepository credentials;
    private final ITokenRepository tokens;
    private final IUserSettingsRepository settings;
    private final Map<String, IAuthManager> typeToManager;

    @Inject
    public AuthResource(
            final INonceRepository nonces,
            final IClientConfigRepository credentials,
            final ITokenRepository tokens,
            final IUserSettingsRepository settings,
            final Map<String, IAuthManager> typeToManager) {
        this.nonces = nonces;
        this.credentials = credentials;
        this.tokens = tokens;
        this.typeToManager = typeToManager;
        this.settings = settings;
    }

    @GET("/oauth/{type}")
    @StatusCode(302)
    public IResponse getAuthUrl(@PathParam("type") final String inputType) {
        return Response.builder()
                .withHeaders("Location", typeToManager.get(inputType).getAuthUrl(nonces.generate(inputType).getId()))
                .build();
    }

    @GET("/oauth/{type}/complete")
    @StatusCode(302)
    public IResponse completeAuth(
            @PathParam("type") final String inputType,
            @QueryParam("code") final String code,
            @QueryParam("error") final String error,
            @QueryParam("state") final String state) {
        return nonces.verify(state, inputType).map(nonce -> {
            final IExpiringAuthManager login = (IExpiringAuthManager) typeToManager.get(inputType);
            return Optional.ofNullable(code)
                    .map(persistToken(inputType, login))
                    .map(token -> Response.redirect("/")
                            .withCookies(CookieImpl.builder()
                                    .withPath("/")
                                    .withName(SESSION_NAME)
                                    .withExpires(new Date(token.getExpiresIn() * 1000))
                                    .withValue(token.getAccessToken())
                                    .build())
                            .build())
                    .orElseThrow(() -> new UnauthorizedException(error));
        })
        .orElseThrow(UnauthorizedException::new);
    }

    private Function<String, IExpiringToken> persistToken(final String type, final IExpiringAuthManager login) {
        return code -> {
            final IExpiringToken token = login.exchange(code);
            final IProfile profile = login.me(token);
            final IUserClientConfig creds = credentials.getByOwner(profile.getEmail(), ServiceAuthorizer.API_TYPE)
                    .orElseGet(() -> credentials.generate(profile.getEmail(), ServiceAuthorizer.API_TYPE));
            final IUserSettings userSettings = settings.get(profile.getEmail())
                    .orElseGet(() -> {
                        final UserSettings us = new UserSettings();
                        us.setEmail(profile.getEmail());
                        us.setPhoto(profile.getImage());
                        us.setFirstName(profile.getFirstName());
                        us.setLastName(profile.getLastName());
                        return us;
                    });
            settings.put(userSettings);
            return tokens.generate(creds);
        };
    }
}
