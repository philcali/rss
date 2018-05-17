package me.philcali.rss.dynamo;

import java.util.Optional;

import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.dynamo.IRetrievalStrategy;
import me.philcali.db.dynamo.QueryRetrievalStrategy;
import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.IFeedRepository;

public class FeedRepositoryDynamo implements IFeedRepository {
    private final Table feeds;
    private final IRetrievalStrategy retrievalStrategy;
    
    public FeedRepositoryDynamo(final Table feeds) {
        this(feeds, QueryRetrievalStrategy.fromTable(feeds));
    }
    
    public FeedRepositoryDynamo(final Table feeds, final IRetrievalStrategy retrievalStrategy) {
        this.feeds = feeds;
        this.retrievalStrategy = retrievalStrategy;
    }
    
    @Override
    public QueryResult<IFeed> list(final QueryParams params) {
        return null;
    }
    
    @Override
    public Optional<IFeed> get(final String id) {
        return Optional.empty();
    }
    
    @Override
    public void put(final IFeed feed) {
    }
}
