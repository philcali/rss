package me.philcali.rss.api;

import java.util.Optional;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;

public interface IArticleInstanceRepository {
    Optional<IArticleInstance> get(String feedId, String articleId, String userId);

    QueryResult<IArticleInstance> list(QueryParams params);

    void put(IArticleInstance instance);
}
