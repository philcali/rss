package me.philcali.rss.module;

import javax.inject.Singleton;

import dagger.Component;
import me.philcali.rss.service.AuthResource;
import me.philcali.rss.service.OPMLResource;
import me.philcali.rss.service.ServiceAuthorizer;
import me.philcali.service.annotations.Resource;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.TokenFilter;
import me.philcali.service.assets.AssetResource;
import me.philcali.service.binding.auth.BearerTokenFilter;

@Component(modules = { DynamoModule.class, AuthenticationModule.class, DocumentModule.class, AssetModule.class })
@Singleton
public interface SmartRSSModule {
    @Resource
    AuthResource createAuthResource();

    @Resource
    OPMLResource createOPMLResource();

    @Resource
    AssetResource createAssetResource();

    @Authorizer
    @TokenFilter(BearerTokenFilter.class)
    ServiceAuthorizer createAuthorizer();
}
