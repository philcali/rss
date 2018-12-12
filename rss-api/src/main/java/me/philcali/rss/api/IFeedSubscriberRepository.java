package me.philcali.rss.api;

import me.philcali.db.api.Conditions;
import me.philcali.db.api.IPageKey;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;

public interface IFeedSubscriberRepository {
    int DEFAULT_SIZE = 10;

    void subscribe(String userId, String feedId);

    void unsubscribe(String userId, String feedId);

    QueryResult<IFeedSubscriber> list(QueryParams params);

    default QueryResult<IFeedSubscriber> listByUser(final String userId, final IPageKey nextToken) {
        return list(QueryParams.builder()
                .withMaxSize(DEFAULT_SIZE)
                .withToken(nextToken)
                .withConditions(Conditions.attribute("userId").equalsTo(userId))
                .build());
    }

    default QueryResult<IFeedSubscriber> listByFeed(final String feedId, final IPageKey nextToken) {
        return list(QueryParams.builder()
                .withMaxSize(DEFAULT_SIZE)
                .withToken(nextToken)
                .withConditions(Conditions.attribute("feedId").equalsTo(feedId))
                .build());
    }
}
