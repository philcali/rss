package me.philcali.rss.module;

import javax.inject.Singleton;

import dagger.Component;
import me.philcali.rss.service.MeResource;
import me.philcali.rss.service.OPMLResource;
import me.philcali.rss.service.ServiceAuthorizer;
import me.philcali.service.annotations.Resource;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.TokenFilter;
import me.philcali.service.binding.auth.BearerTokenFilter;

@Component(modules = {
        DynamoModule.class,
        ConfigModule.class,
        AuthenticationModule.class,
        UserModule.class,
        DocumentModule.class
})
@Singleton
public interface PetroniusServiceModule {
    @Resource
    MeResource createMeResource();

    @Resource
    OPMLResource createOPMLResource();

    @Authorizer
    @TokenFilter(BearerTokenFilter.class)
    ServiceAuthorizer createAuthorizer();
}
