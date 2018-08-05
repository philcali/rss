package me.philcali.rss.module;

import javax.inject.Singleton;

import dagger.Component;
import me.philcali.service.annotations.Resource;
import me.philcali.service.assets.AssetResource;

@Component(modules = {
    ConfigModule.class,
    HttpModule.class,
    AssetModule.class
})
@Singleton
public interface PetroniusWebsiteAssetModule {
    @Resource
    AssetResource createAssetResource();
}
