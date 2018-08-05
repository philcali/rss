package me.philcali.rss.module;

import javax.inject.Singleton;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import dagger.Module;
import dagger.Provides;

@Module
public class S3Module {

    @Provides
    @Singleton
    static AmazonS3 providesAmazonS3() {
        return AmazonS3ClientBuilder.defaultClient();
    }
}
