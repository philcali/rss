package me.philcali.rss.module;

import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.service.assests.proxy.IProxyLocationConfig;
import org.service.assests.proxy.ProxyAssetLoader;

import dagger.Module;
import dagger.Provides;
import me.philcali.config.api.IConfigFactory;
import me.philcali.http.api.IHttpClient;
import me.philcali.http.java.NativeHttpClient;
import me.philcali.service.assets.AssetResource;
import me.philcali.service.assets.IAssetLoader;

@Module
public class AssetModule {
    private static final int MAX_AGE = 5;
    private static final String LOCATION = "Location";

    @Provides
    @Singleton
    static IHttpClient providesHttpClient() {
        return new NativeHttpClient();
    }

    @Provides
    @Singleton
    static IProxyLocationConfig providesLocationConfig(final IConfigFactory factory) {
        return factory.create(IProxyLocationConfig.class, Optional.of(LOCATION));
    }

    @Provides
    @Singleton
    static IAssetLoader providesAssetLoader(final IHttpClient client, final IProxyLocationConfig config) {
        return new ProxyAssetLoader(client, config);
    }

    @Provides
    @Singleton
    @Named(AssetResource.MAX_AGE_NAME)
    static int providesMaxAge() {
        return MAX_AGE;
    }

    @Provides
    @Singleton
    @Named(AssetResource.INDEX_DOCUMENT)
    static String providesIndexDocument() {
        return "";
    }
}
