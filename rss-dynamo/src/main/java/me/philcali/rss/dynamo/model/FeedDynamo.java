package me.philcali.rss.dynamo.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.rss.api.IFeed;

public class FeedDynamo implements IFeed {
    private final Item item;

    public FeedDynamo(final Item item) {
        this.item = item;
    }

    @Override
    public String getId() {
        return item.getString("id");
    }

    @Override
    public String getTitle() {
        return item.getString("title");
    }

    @Override
    public String getDescription() {
        return item.getString("description");
    }

    @Override
    public String getUri() {
        return item.getString("uri");
    }

    @Override
    public String getHtmlUri() {
        return item.getString("htmlUri");
    }

    @Override
    public Date getUpdatedAt() {
        return new Date(item.getLong("updatedAt"));
    }

    @Override
    public List<String> getCategories() {
        return item.getList("categories");
    }

    @Override
    public Map<String, String> getMetadata() {
        return item.getMap("metadata");
    }
}
