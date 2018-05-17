package me.philcali.rss.api;

import java.util.Optional;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;

public interface IFeedRepository {
    QueryResult<IFeed> list(QueryParams params);
    
    Optional<IFeed> get(String id);
    
    void put(IFeed feed);
}
