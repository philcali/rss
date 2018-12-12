package me.philcali.rss.service;

import javax.inject.Inject;

import me.philcali.db.api.PageKey;
import me.philcali.rss.api.IFeedRepository;
import me.philcali.rss.api.IFeedSubscriber;
import me.philcali.rss.api.IFeedSubscriberRepository;
import me.philcali.rss.service.model.ListResourceResponse;
import me.philcali.service.annotations.DELETE;
import me.philcali.service.annotations.GET;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.AuthorizerParam;
import me.philcali.service.annotations.request.ParamFilter;
import me.philcali.service.annotations.request.PathParam;
import me.philcali.service.annotations.request.QueryParam;
import me.philcali.service.reflection.filter.Base64Decode;

public class FeedResource {
    private final IFeedSubscriberRepository subscribers;
    private final IFeedRepository feeds;

    @Inject
    public FeedResource(
            final IFeedSubscriberRepository subscribers,
            final IFeedRepository feeds) {
        this.subscribers = subscribers;
        this.feeds = feeds;
    }

    @GET("/feeds")
    @Authorizer(ServiceAuthorizer.class)
    public ListResourceResponse<IFeedSubscriber> getSubscribedFeeds(
            @AuthorizerParam("userId") final String userId,
            @ParamFilter(Base64Decode.class)
            @QueryParam("nextToken") final PageKey nextToken) {
        return new ListResourceResponse<>(subscribers.listByUser(userId, nextToken));
    }

    @DELETE("/feeds/{feedId}")
    @Authorizer(ServiceAuthorizer.class)
    public void unsubscribe(
            @AuthorizerParam("userId") final String userId,
            @PathParam("feedId") final String feedId) {
        subscribers.unsubscribe(userId, feedId);
    }
}
