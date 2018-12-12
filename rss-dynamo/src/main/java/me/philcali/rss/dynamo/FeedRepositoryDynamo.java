package me.philcali.rss.dynamo;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.dynamo.BatchGetAdapter;
import me.philcali.db.dynamo.IRetrievalStrategy;
import me.philcali.db.dynamo.QueryRetrievalStrategy;
import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.IFeedRepository;
import me.philcali.rss.api.exception.FeedStorageException;
import me.philcali.rss.dynamo.model.FeedDynamo;

public class FeedRepositoryDynamo implements IFeedRepository {
    private final DynamoDB db;
    private final Table feeds;
    private final IRetrievalStrategy retrievalStrategy;

    public FeedRepositoryDynamo(final DynamoDB db, final Table feeds) {
        this(db, feeds, QueryRetrievalStrategy.fromTable(feeds));
    }

    public FeedRepositoryDynamo(
            final DynamoDB db,
            final Table feeds,
            final IRetrievalStrategy retrievalStrategy) {
        this.db = db;
        this.feeds = feeds;
        this.retrievalStrategy = retrievalStrategy;
    }

    @Override
    public QueryResult<IFeed> list(final QueryParams params) {
        return retrievalStrategy.apply(params, feeds).map(FeedDynamo::new);
    }

    @Override
    public Optional<IFeed> get(final String id) {
        return Optional.ofNullable(feeds.getItem("id", id)).map(FeedDynamo::new);
    }

    @Override
    public List<IFeed> batchGet(final List<String> feedIds) {
        final Function<List<PrimaryKey>, List<IFeed>> adapter = new BatchGetAdapter(db, feeds.getTableName())
                .andThen(result -> result.stream().map(FeedDynamo::new).collect(Collectors.toList()));
        return adapter.apply(feedIds.stream()
                .map(feedId -> new PrimaryKey().addComponent("feedId", feedId))
                .collect(Collectors.toList()));
    }

    @Override
    public void put(final IFeed feed) {
        final Item item = new Item()
                .withString("id", feed.getId())
                .withString("title", feed.getTitle())
                .withString("uri", feed.getUri())
                .withString("htmlUri", feed.getHtmlUri())
                .withLong("updatedAt", feed.getUpdatedAt().getTime());
        Optional.ofNullable(feed.getDescription()).ifPresent(description -> {
            item.withString("description", description);
        });
        Optional.ofNullable(feed.getCategories()).ifPresent(categories -> {
            item.withList("categories", categories);
        });
        Optional.ofNullable(feed.getMetadata()).ifPresent(metadata -> {
            item.withMap("metadata", metadata);
        });
        try {
            feeds.putItem(item);
        } catch (SdkBaseException e) {
            throw new FeedStorageException(e);
        }
    }
}
