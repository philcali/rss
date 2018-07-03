package me.philcali.rss.module;

import javax.inject.Singleton;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import dagger.Module;
import dagger.Provides;
import me.philcali.rss.api.IFeedRepository;
import me.philcali.rss.api.ompl.IDocumentFactory;
import me.philcali.rss.dynamo.FeedRepositoryDynamo;
import me.philcali.rss.opml.DocumentFactoryImpl;

@Module
public class DocumentModule {
    public static final String FEEDS_TABLE = "RSS.Feeds";

    @Provides
    @Singleton
    static IDocumentFactory providesDocumentFactory() {
        return new DocumentFactoryImpl();
    }

    @Provides
    @Singleton
    static IFeedRepository providesFeedRepository(final DynamoDB dynamoDb) {
        return new FeedRepositoryDynamo(dynamoDb.getTable(FEEDS_TABLE));
    }
}
