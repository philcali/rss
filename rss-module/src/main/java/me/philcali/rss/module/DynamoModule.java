package me.philcali.rss.module;

import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import dagger.Module;
import dagger.Provides;


@Module
public class DynamoModule {
    @Provides
    @Singleton
    static AmazonDynamoDB providesAmazonDynamoDBClient() {
        return AmazonDynamoDBClientBuilder.defaultClient();
    }

    @Provides
    @Singleton
    static DynamoDB providesDynamoDB(final AmazonDynamoDB client) {
        return new DynamoDB(client);
    }
}
