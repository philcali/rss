package me.philcali.rss.dynamo.model;

import java.util.Date;
import java.util.Optional;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.rss.api.IArticleInstance;

public class ArticleInstanceDynamo implements IArticleInstance {
    private final Item item;

    public ArticleInstanceDynamo(final Item item) {
        this.item = item;
    }

    @Override
    public String getArticleId() {
        return item.getString("articleId");
    }

    @Override
    public String getFeedId() {
        return item.getString("feedId");
    }

    @Override
    public String getUserId() {
        return item.getString("userId");
    }

    @Override
    public Date getPublicationDate() {
        return new Date(item.getLong("publicationDate"));
    }

    @Override
    public Date getReadDate() {
        return Optional.ofNullable(item.getLong("readDate")).map(Date::new).orElse(null);
    }

    @Override
    public Date getArchivedDate() {
        return Optional.ofNullable(item.getLong("archivedDate")).map(Date::new).orElse(null);
    }

    @Override
    public boolean isFlagged() {
        return item.getBoolean("flagged");
    }

    @Override
    public boolean isRead() {
        return item.getBoolean("read");
    }

    @Override
    public boolean isArchived() {
        return item.getBoolean("archived");
    }

}
