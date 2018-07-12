package me.philcali.rss.module;

import javax.inject.Singleton;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;

import dagger.Module;
import dagger.Provides;


@Module
public class SystemManagerModule {
    @Provides
    @Singleton
    static AWSSimpleSystemsManagement providesSimpleSystemManager() {
        return AWSSimpleSystemsManagementClientBuilder.defaultClient();
    }
}
