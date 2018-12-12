package me.philcali.rss.service;

import javax.inject.Inject;

import me.philcali.db.api.PageKey;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.IFeedRepository;
import me.philcali.rss.service.model.ListResourceResponse;
import me.philcali.service.annotations.GET;
import me.philcali.service.annotations.request.DefaultValue;
import me.philcali.service.annotations.request.ParamFilter;
import me.philcali.service.annotations.request.QueryParam;
import me.philcali.service.annotations.request.Validation;
import me.philcali.service.annotations.request.Validations;
import me.philcali.service.reflection.filter.Base64Decode;

public class NewsResource {
    private static final String DEFAULT_LIMIT = "10";
    private final IFeedRepository feeds;

    @Inject
    public NewsResource(final IFeedRepository feeds) {
        this.feeds = feeds;
    }

    @GET("/news")
    public ListResourceResponse<IFeed> list(
            @DefaultValue(DEFAULT_LIMIT)
            @Validations(@Validation(required = false, min = 1, max = 100))
            @QueryParam("limit") final int limit,
            @ParamFilter(Base64Decode.class)
            @QueryParam("nextToken") final PageKey nextToken) {
        final QueryResult<IFeed> results = feeds.list(QueryParams.builder()
                .withMaxSize(limit)
                .withToken(nextToken)
                .build());
        return new ListResourceResponse<>(results);
    }
}
