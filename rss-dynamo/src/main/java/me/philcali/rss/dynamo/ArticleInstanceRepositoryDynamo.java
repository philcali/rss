package me.philcali.rss.dynamo;

import java.util.Optional;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.dynamo.QueryRetrievalStrategy;
import me.philcali.rss.api.IArticleInstance;
import me.philcali.rss.api.IArticleInstanceRepository;
import me.philcali.rss.api.exception.ArticleInstanceStorageException;
import me.philcali.rss.dynamo.model.ArticleInstanceDynamo;

public class ArticleInstanceRepositoryDynamo implements IArticleInstanceRepository {
    private static final String COMPOSITE_KEY_FORMAT = "%s:%s";
    private final Table table;
    private final QueryRetrievalStrategy retrieval;

    public ArticleInstanceRepositoryDynamo(final Table table, final QueryRetrievalStrategy retrieval) {
        this.table = table;
        this.retrieval = retrieval;
    }

    public ArticleInstanceRepositoryDynamo(final Table table) {
        this(table, QueryRetrievalStrategy.fromTable(table));
    }

    @Override
    public Optional<IArticleInstance> get(final String feedId, final String articleId, final String userId) {
        return Optional.ofNullable(table.getItem(
                "userId", userId,
                "compositeKey", String.format(COMPOSITE_KEY_FORMAT, feedId, articleId)))
                .map(ArticleInstanceDynamo::new);
    }

    @Override
    public QueryResult<IArticleInstance> list(final QueryParams params) {
        return retrieval.apply(params, table).map(ArticleInstanceDynamo::new);
    }

    @Override
    public void put(final IArticleInstance instance) {
        final Item item = new Item()
                .withString("compositeKey", String.format(COMPOSITE_KEY_FORMAT,
                        instance.getFeedId(),
                        instance.getArticleId()))
                .withString("feedId", instance.getFeedId())
                .withString("articleId", instance.getArticleId())
                .withString("userId", instance.getUserId())
                .withBoolean("flagged", instance.isFlagged())
                .withBoolean("archived", instance.isArchived())
                .withBoolean("read", instance.isRead())
                .withLong("publicationDate", instance.getPublicationDate().getTime());
        Optional.ofNullable(instance.getReadDate()).ifPresent(readDate -> {
            item.withLong("readDate", readDate.getTime());
        });
        Optional.ofNullable(instance.getArchivedDate()).ifPresent(archiveDate -> {
            item.withLong("archivedDate", archiveDate.getTime());
        });
        try {
            table.putItem(item);
        } catch (SdkBaseException e) {
            throw new ArticleInstanceStorageException(e);
        }
    }

}
