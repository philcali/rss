package me.philcali.rss.module;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;
import me.philcali.config.api.IConfigFactory;
import me.philcali.config.api.IConfigProvider;
import me.philcali.config.api.chain.DefaultConfigProviderChain;
import me.philcali.config.cache.CacheInitializationPolicy;
import me.philcali.config.cache.CachedConfigProvider;
import me.philcali.config.cache.ICacheUpdateStrategy;
import me.philcali.config.cache.event.CacheEventPublisher;
import me.philcali.config.cache.event.CacheEventType;
import me.philcali.config.cache.event.CacheFanOutCommandExecution;
import me.philcali.config.cache.event.ICacheEventPublisher;
import me.philcali.config.cache.update.CacheConfigUpdateStrategyBuilder;
import me.philcali.config.cache.update.CacheEvictionPolicy;
import me.philcali.config.proxy.ConfigProxyFactory;
import me.philcali.config.proxy.ConfigProxyFactoryOptions;
import me.philcali.config.proxy.name.DefaultParameterGroupPrefix;

@Module
public class ConfigModule {
    public static final String ENV = "Prod";
    public static final String APPLICATION_NAME = "SmartRSS";

    @Provides
    @Singleton
    static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    static ICacheEventPublisher providesCacheEvents(final CacheFanOutCommandExecution executions) {
        return new CacheEventPublisher().subscribe(CacheEventType.CREATED, executions);
    }

    @Provides
    @Singleton
    static CacheFanOutCommandExecution providesCacheTrackingQueue() {
        return new CacheFanOutCommandExecution();
    }

    @Provides
    @Singleton
    static ICacheUpdateStrategy providesCacheUpdateStrategy(final CacheFanOutCommandExecution executions) {
        return CacheConfigUpdateStrategyBuilder.neverUpdating()
                .withEvictionPolicy(CacheEvictionPolicy.TIME_BASED)
                .withEvictionTimeUnit(TimeUnit.SECONDS)
                .withEvictionAmount(30)
                .withCommandExecution(executions)
                .build();
    }

    @Provides
    @Singleton
    static IConfigProvider providesCachingConfigProvider(final ICacheEventPublisher events,
            final ICacheUpdateStrategy cacheUpdate) {
        return CachedConfigProvider.builder()
                .withConfigProvider(new DefaultConfigProviderChain())
                .withEventPublisher(events)
                .withInitializationPolicy(CacheInitializationPolicy.FILL)
                .withUpdateStrategy(cacheUpdate)
                .build();
    }

    @Provides
    @Singleton
    static IConfigFactory providesConfigFactory(final IConfigProvider provider) {
        return new ConfigProxyFactory(ConfigProxyFactoryOptions.builder()
                .withConfigProvider(provider)
                .withGroupPrefix(new DefaultParameterGroupPrefix()
                        .addPart(ENV)
                        .addPart(APPLICATION_NAME))
                .build());
    }


}
