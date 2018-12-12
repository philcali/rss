package me.philcali.rss.module;

import javax.inject.Singleton;

import dagger.Component;
import me.philcali.rss.service.AuthResource;
import me.philcali.service.annotations.Resource;

@Component(modules = {
        DynamoModule.class,
        ConfigModule.class,
        JacksonModule.class,
        AuthenticationModule.class,
        UserModule.class
})
@Singleton
public interface PetroniusWebsiteModule {
    @Resource
    AuthResource createAuthResource();
}
