package me.philcali.rss.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import me.philcali.oauth.api.IClientConfigRepository;
import me.philcali.oauth.api.ITokenRepository;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.binding.IOperation;

@Authorizer
public class ServiceAuthorizer implements IOperation<String, Map<String, String>> {
    public static final String API_TYPE = "SMART_RSS";
    private static final String CLIENT_ID = "clientId";
    private static final String USER_ID = "userId";
    private final ITokenRepository tokens;
    private final IClientConfigRepository credentials;
    
    @Inject
    public ServiceAuthorizer(final ITokenRepository tokens, final IClientConfigRepository credentials) {
        this.tokens = tokens;
        this.credentials = credentials;
    }
    
    @Override
    public Map<String, String> apply(final String token) {
        return tokens.get(API_TYPE, token)
                .flatMap(session -> credentials.get(session.getApi(), session.getClientId()))
                .map(creds -> {
                    final Map<String, String> properties = new HashMap<>();
                    properties.put(CLIENT_ID, creds.getClientId());
                    properties.put(USER_ID, creds.getUserId());
                    return properties;
                })
                .orElseGet(Collections::emptyMap);
    }
}
