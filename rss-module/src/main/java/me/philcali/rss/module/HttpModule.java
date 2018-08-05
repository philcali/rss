package me.philcali.rss.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.philcali.http.api.IHttpClient;
import me.philcali.http.java.NativeHttpClient;

@Module
public class HttpModule {
    @Provides
    @Singleton
    static IHttpClient providesHttpClient() {
        return new NativeHttpClient();
    }
}
