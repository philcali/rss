package me.philcali.rss.module;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import me.philcali.service.assets.AssetResource;
import me.philcali.service.assets.IAssetLoader;
import me.philcali.service.assets.LocalAssetLoader;


@Module
public class AssetModule {
    private static final int DEFAULT_MAX_AGE = 86400;

    @Provides
    public IAssetLoader getAssetLoader() {
        return new LocalAssetLoader();
    }

    @Provides
    @Named(AssetResource.MAX_AGE_NAME)
    public int getMaxAge() {
        return DEFAULT_MAX_AGE;
    }
}
