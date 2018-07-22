package me.philcali.rss.service;

import javax.inject.Inject;

import me.philcali.oauth.api.IClientConfigRepository;
import me.philcali.oauth.api.ITokenRepository;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.binding.IOperation;
import me.philcali.service.binding.auth.AuthResult;
import me.philcali.service.binding.auth.IAuthResult;
import me.philcali.service.binding.response.UnauthorizedException;

@Authorizer
public class ServiceAuthorizer implements IOperation<String, IAuthResult> {
    public static final String API_TYPE = "SMART_RSS";
    private static final String USER_ID = "userId";
    private static final String ACCESS_TOKEN = "accessToken";
    private final ITokenRepository tokens;
    private final IClientConfigRepository credentials;

    @Inject
    public ServiceAuthorizer(final ITokenRepository tokens, final IClientConfigRepository credentials) {
        this.tokens = tokens;
        this.credentials = credentials;
    }

    @Override
    public IAuthResult apply(final String token) {
        return tokens.get(API_TYPE, token)
                .flatMap(session -> credentials.get(session.getApi(), session.getClientId()))
                .map(creds -> AuthResult.builder()
                        .withPrincipalId(creds.getClientId())
                        .addContext(USER_ID, creds.getUserId())
                        .addContext(ACCESS_TOKEN, token)
                        .build())
                .orElseThrow(UnauthorizedException::new);
    }
}
