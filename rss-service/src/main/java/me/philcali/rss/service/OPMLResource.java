 package me.philcali.rss.service;

import java.util.ArrayList;
import java.util.function.Function;

import javax.inject.Inject;

import me.philcali.rss.api.IArticleInstance;
import me.philcali.rss.api.IArticleInstanceRepository;
import me.philcali.rss.api.IArticleRepository;
import me.philcali.rss.api.IFeedContainer;
import me.philcali.rss.api.IFeedContainerFactory;
import me.philcali.rss.api.IFeedRepository;
import me.philcali.rss.api.IFeedSubscriberRepository;
import me.philcali.rss.api.IOutlineStream;
import me.philcali.rss.api.exception.ArticleInstanceStorageException;
import me.philcali.rss.api.exception.ArticleStorageException;
import me.philcali.rss.api.exception.FeedContainerCreationException;
import me.philcali.rss.api.exception.OutlineStreamException;
import me.philcali.rss.api.model.ArticleInstance;
import me.philcali.rss.api.ompl.IDocument;
import me.philcali.rss.api.ompl.IDocumentFactory;
import me.philcali.rss.api.ompl.model.Outline;
import me.philcali.rss.service.model.FeedImportRequest;
import me.philcali.rss.service.model.FeedImportResponse;
import me.philcali.rss.service.model.FeedImportResponse.FeedImportResult;
import me.philcali.service.annotations.POST;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.AuthorizerParam;
import me.philcali.service.annotations.request.Body;

public class OPMLResource {
    private final IDocumentFactory documentFactory;
    private final IFeedContainerFactory feedFactory;
    private final IOutlineStream outlineStream;
    private final IFeedRepository feeds;
    private final IArticleRepository articles;
    private final IFeedSubscriberRepository subscribers;
    private final IArticleInstanceRepository instances;

    @Inject
    public OPMLResource(
            final IDocumentFactory documentFactory,
            final IFeedContainerFactory feedFactory,
            final IOutlineStream outlineStream,
            final IFeedRepository feeds,
            final IArticleRepository articles,
            final IFeedSubscriberRepository subscribers,
            final IArticleInstanceRepository instances) {
        this.documentFactory = documentFactory;
        this.feedFactory = feedFactory;
        this.outlineStream = outlineStream;
        this.feeds = feeds;
        this.articles = articles;
        this.subscribers = subscribers;
        this.instances = instances;
    }

    @POST("/opml/import")
    @Authorizer(ServiceAuthorizer.class)
    public IDocument omplImport(@Body final String contents) {
        return documentFactory.create(contents);
    }

    @POST("/opml/preview")
    @Authorizer(ServiceAuthorizer.class)
    public IFeedContainer omplPreview(@Body final Outline outline) {
        return outlineStream.andThen(feedFactory::create).apply(outline.getXmlUrl());
    }

    @POST("/opml/feeds")
    @Authorizer(ServiceAuthorizer.class)
    public FeedImportResponse omplFeeds(@Body final FeedImportRequest request, @AuthorizerParam("userId") final String userId) {
        final Function<String, IFeedContainer> pullLatest = outlineStream.andThen(feedFactory::create);
        final FeedImportResponse response = new FeedImportResponse(new ArrayList<>(request.getOutlines().size()));
        request.getOutlines().forEach(outline -> {
            final FeedImportResult result = new FeedImportResult(outline.getTitle());
            try {
                final IFeedContainer container = pullLatest.apply(outline.getXmlUrl());
                feeds.put(container.getFeed());
                subscribers.subscribe(userId, container.getFeed().getId());
                container.getArticles().forEach(article -> {
                    final String feedId = container.getFeed().getId();
                    articles.put(feedId, article);
                    final IArticleInstance instance = instances.get(feedId, article.getId(), userId)
                            .orElseGet(() -> ArticleInstance.builder()
                                    .withArticleId(article.getId())
                                    .withFeedId(feedId)
                                    .withUserId(userId)
                                    .withPublicationDate(article.getUpdatedAt())
                                    .build());
                    instances.put(instance);
                });
            } catch (OutlineStreamException
                    | ArticleStorageException
                    | ArticleInstanceStorageException
                    | FeedContainerCreationException ex) {
                result.setError(true);
                result.setErrorMessage(ex.getMessage());
            } finally {
                response.addResult(result);
            }
        });
        return response;
    }
}
