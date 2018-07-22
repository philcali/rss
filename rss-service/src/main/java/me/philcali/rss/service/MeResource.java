package me.philcali.rss.service;

import java.util.Map;

import javax.inject.Inject;

import me.philcali.oauth.api.IAuthManager;
import me.philcali.oauth.api.ITokenRepository;
import me.philcali.oauth.api.model.IProfile;
import me.philcali.service.annotations.GET;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.AuthorizerParam;
import me.philcali.service.binding.response.NotFoundException;

public class MeResource {
    private final ITokenRepository tokens;
    private final Map<String, IAuthManager> typesToManager;

    @Inject
    public MeResource(
            final ITokenRepository tokens,
            final Map<String, IAuthManager> typesToManager) {
        this.tokens = tokens;
        this.typesToManager = typesToManager;
    }

    @GET("/me")
    @Authorizer(ServiceAuthorizer.class)
    public IProfile me(@AuthorizerParam("accessToken") final String accessToken) {
        // Pull reference and query user authority
        // I'm not fond of this approach, but it's better than storing multiple user references
        return tokens.get(ServiceAuthorizer.API_TYPE, accessToken)
                .flatMap(token -> tokens.get(
                        token.getParams().get("type"),
                        token.getParams().get("accessToken")))
                .map(token -> typesToManager.get(token.getApi()).me(token))
                .orElseThrow(NotFoundException::new);
    }
}
