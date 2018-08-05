package me.philcali.rss.module;

import java.util.function.Supplier;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.xml.parsers.SAXParserFactory;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;

import dagger.Module;
import dagger.Provides;
import me.philcali.http.api.IHttpClient;
import me.philcali.rss.api.IArticleInstanceRepository;
import me.philcali.rss.api.IArticleRepository;
import me.philcali.rss.api.IFeedContainerFactory;
import me.philcali.rss.api.IFeedRepository;
import me.philcali.rss.api.IFeedSubscriberRepository;
import me.philcali.rss.api.IOutlineStream;
import me.philcali.rss.api.ompl.IDocumentFactory;
import me.philcali.rss.atom.AtomContentHandler;
import me.philcali.rss.atom.ContentEncodedExtension;
import me.philcali.rss.atom.FeedContainerFactoryImpl;
import me.philcali.rss.atom.FeedburnerOrigLinkExtension;
import me.philcali.rss.atom.HttpOutlineStream;
import me.philcali.rss.dynamo.ArticleInstanceRepositoryDynamo;
import me.philcali.rss.dynamo.ArticleRepositoryDynamo;
import me.philcali.rss.dynamo.FeedRepositoryDynamo;
import me.philcali.rss.dynamo.FeedSubscriberRepositoryDynamo;
import me.philcali.rss.opml.DocumentFactoryImpl;
import me.philcali.rss.s3.ArticleRepositoryS3;


@Module
public class DocumentModule {
    public static final String ARTICLE_BUCKET = "petronius-articles";
    public static final String FEEDS_TABLE = "RSS.Feeds";
    public static final String FEED_SUBSCRIBERS_TABLE = "RSS.Subscribers";
    public static final String ARTICLE_TABLE = "RSS.Articles";
    public static final String ARTICLE_INSTANCES_TABLE = "RSS.ArticleInstances";
    public static final String CONTENT_REPO_NAME = "content.repo";

    @Provides
    @Singleton
    static SAXParserFactory providesSAXFactory() {
        return SAXParserFactory.newInstance();
    }

    @Provides
    @Singleton
    static Supplier<AtomContentHandler> providesContentHandlerFactory() {
        return () -> new AtomContentHandler()
                .extend(new ContentEncodedExtension())
                .extend(new FeedburnerOrigLinkExtension());
    }

    @Provides
    @Singleton
    static IDocumentFactory providesDocumentFactory(final SAXParserFactory factory) {
        return new DocumentFactoryImpl(factory);
    }

    @Provides
    @Singleton
    static IFeedContainerFactory providesFeedContainerFactory(
            final SAXParserFactory factory,
            final Supplier<AtomContentHandler> contentHandlerFactory) {
        return new FeedContainerFactoryImpl(factory, contentHandlerFactory);
    }

    @Provides
    @Singleton
    static IOutlineStream providesOutlineStream(final IHttpClient client) {
        return new HttpOutlineStream(client);
    }

    @Provides
    @Singleton
    static IFeedRepository providesFeedRepository(final DynamoDB dynamoDb) {
        return new FeedRepositoryDynamo(dynamoDb.getTable(FEEDS_TABLE));
    }

    @Provides
    @Singleton
    static IFeedSubscriberRepository providesFeedSubscriberRepository(final DynamoDB dynamoDb) {
        return new FeedSubscriberRepositoryDynamo(dynamoDb.getTable(FEED_SUBSCRIBERS_TABLE));
    }

    @Provides
    @Singleton
    static IArticleInstanceRepository providesArticleInstanceRepository(final DynamoDB dynamoDb) {
        return new ArticleInstanceRepositoryDynamo(dynamoDb.getTable(ARTICLE_INSTANCES_TABLE));
    }

    @Provides
    @Singleton
    @Named(CONTENT_REPO_NAME)
    static IArticleRepository providesArticleRepository(final AmazonS3 s3, final ObjectMapper mapper) {
        return new ArticleRepositoryS3(ARTICLE_BUCKET, s3, mapper);
    }

    @Provides
    @Singleton
    static IArticleRepository providesIndexedArticleRepostory(
            final DynamoDB dynamoDb,
            @Named(CONTENT_REPO_NAME) final IArticleRepository contentRepo) {
        return new ArticleRepositoryDynamo(dynamoDb.getTable(ARTICLE_TABLE), contentRepo);
    }
}
