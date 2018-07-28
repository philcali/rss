package me.philcali.rss.service;

import javax.inject.Inject;

import me.philcali.rss.api.IUserSettings;
import me.philcali.rss.api.IUserSettingsRepository;
import me.philcali.service.annotations.GET;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.AuthorizerParam;
import me.philcali.service.binding.response.NotFoundException;

public class MeResource {
    private final IUserSettingsRepository settings;

    @Inject
    public MeResource(final IUserSettingsRepository settings) {
        this.settings = settings;
    }

    @GET("/me")
    @Authorizer(ServiceAuthorizer.class)
    public IUserSettings me(@AuthorizerParam("userId") final String email) {
        return settings.get(email).orElseThrow(NotFoundException::new);
    }
}
