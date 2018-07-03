package me.philcali.rss.module;

import dagger.Component;
import me.philcali.rss.service.AuthResource;
import me.philcali.rss.service.OPMLResource;
import me.philcali.service.annotations.Resource;

@Component(modules = { DynamoModule.class, AuthenticationModule.class, DocumentModule.class })
public interface SmartRSSModule {
    @Resource
    AuthResource createAuthResource();

    @Resource
    OPMLResource createOPMLResource();
}
