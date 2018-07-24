package me.philcali.rss.module;

import javax.inject.Singleton;

import dagger.Component;
import me.philcali.rss.service.AuthResource;
import me.philcali.service.annotations.Resource;
import me.philcali.service.assets.AssetResource;

@Component(modules = {
        AssetModule.class,
        DynamoModule.class,
        ConfigModule.class,
        AuthenticationModule.class
})
@Singleton
public interface PetroniusWebsiteModule {
    @Resource
    AuthResource createAuthResource();

    @Resource
    AssetResource createAssetResource();
}
