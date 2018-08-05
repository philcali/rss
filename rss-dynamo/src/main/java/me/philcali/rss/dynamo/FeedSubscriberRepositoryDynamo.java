package me.philcali.rss.dynamo;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.dynamo.QueryRetrievalStrategy;
import me.philcali.rss.api.IFeedSubscriber;
import me.philcali.rss.api.IFeedSubscriberRepository;
import me.philcali.rss.api.exception.FeedSubscriptionException;
import me.philcali.rss.dynamo.model.FeedSubscriberDynamo;

public class FeedSubscriberRepositoryDynamo implements IFeedSubscriberRepository {
    private final Table table;
    private final QueryRetrievalStrategy retrieval;

    public FeedSubscriberRepositoryDynamo(final Table table, final QueryRetrievalStrategy retrieval) {
        this.table = table;
        this.retrieval = retrieval;
    }

    public FeedSubscriberRepositoryDynamo(final Table table) {
        this(table, QueryRetrievalStrategy.fromTable(table));
    }

    @Override
    public void subscribe(final String userId, final String feedId) {
        final Item item = new Item()
                .withString("feedId", feedId)
                .withString("userId", userId);
        try {
            table.putItem(item);
        } catch (SdkBaseException e) {
            throw new FeedSubscriptionException(e);
        }
    }

    @Override
    public void unsubscribe(final String userId, final String feedId) {
        try {
           table.deleteItem(new DeleteItemSpec()
                   .withPrimaryKey(new PrimaryKey()
                           .addComponent("feedId", feedId)
                           .addComponent("userId", userId)));
        } catch (SdkBaseException e) {
            throw new FeedSubscriptionException(e);
        }
    }

    @Override
    public QueryResult<IFeedSubscriber> list(final QueryParams params) {
        return retrieval.apply(params, table).map(FeedSubscriberDynamo::new);
    }
}
