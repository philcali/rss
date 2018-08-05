package me.philcali.rss.api;

import me.philcali.db.api.Conditions;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;

public interface IFeedSubscriberRepository {
    void subscribe(String userId, String feedId);

    void unsubscribe(String userId, String feedId);

    QueryResult<IFeedSubscriber> list(QueryParams params);

    default QueryResult<IFeedSubscriber> listByUser(final String userId) {
        return list(QueryParams.builder()
                .withConditions(Conditions.attribute("userId").equalsTo(userId))
                .build());
    }

    default QueryResult<IFeedSubscriber> listByFeed(final String feedId) {
        return list(QueryParams.builder()
                .withConditions(Conditions.attribute("feedId").equalsTo(feedId))
                .build());
    }
}
