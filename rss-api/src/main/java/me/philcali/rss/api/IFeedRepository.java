package me.philcali.rss.api;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;

public interface IFeedRepository {
    QueryResult<IFeed> list(QueryParams params);
}
