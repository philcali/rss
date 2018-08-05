package me.philcali.rss.api;

import java.util.Optional;

import me.philcali.db.api.Conditions;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;

public interface IArticleRepository {
    void put(String feedId, IArticle article);

    Optional<IArticle> get(String feedId, String articleId);

    QueryResult<IArticle> list(QueryParams params);

    default QueryResult<IArticle> listByFeedId(final String feedId) {
        return list(QueryParams.builder()
                .withConditions(Conditions.attribute("feedId").equalsTo(feedId))
                .build());
    }
}
