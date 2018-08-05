package me.philcali.rss.dynamo;

import java.util.Optional;

import com.amazonaws.SdkBaseException;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.db.dynamo.QueryRetrievalStrategy;
import me.philcali.rss.api.IArticle;
import me.philcali.rss.api.IArticleRepository;
import me.philcali.rss.api.exception.ArticleStorageException;
import me.philcali.rss.dynamo.model.ArticleDynamo;

public class ArticleRepositoryDynamo implements IArticleRepository {
    private final Table table;
    private final QueryRetrievalStrategy retrieval;
    private final IArticleRepository contentRepo;

    public ArticleRepositoryDynamo(
            final Table table,
            final QueryRetrievalStrategy retrieval,
            final IArticleRepository contentRepo) {
        this.table = table;
        this.retrieval = retrieval;
        this.contentRepo = contentRepo;
    }

    public ArticleRepositoryDynamo(final Table table, final IArticleRepository contentRepo) {
        this(table, QueryRetrievalStrategy.fromTable(table), contentRepo);
    }

    @Override
    public void put(final String feedId, final IArticle article) {
        final Item item = new Item()
                .withString("feedId", feedId)
                .withString("articleId", article.getId())
                .withLong("updatedAt", article.getUpdatedAt().getTime())
                .withString("uri", article.getUri())
                .withString("title", article.getTitle())
                .withList("categories", article.getCategories())
                .withMap("metadata", article.getMetadata());
        Optional.ofNullable(article.getCommentsUri()).ifPresent(commentsUri -> {
            item.withString("commentsUri", commentsUri);
        });
        try {
            contentRepo.put(feedId, article);
            table.putItem(item);
        } catch (SdkBaseException e) {
            throw new ArticleStorageException(e);
        }
    }

    @Override
    public Optional<IArticle> get(final String feedId, final String articleId) {
        return Optional.ofNullable(table.getItem("feedId", feedId, "articleId", articleId))
                .map(item -> new ArticleDynamo(item, contentRepo));
    }

    @Override
    public QueryResult<IArticle> list(final QueryParams params) {
        return retrieval.apply(params, table).map(item -> new ArticleDynamo(item, contentRepo));
    }

}
