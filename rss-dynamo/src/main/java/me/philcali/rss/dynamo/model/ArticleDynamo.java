package me.philcali.rss.dynamo.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.rss.api.IArticle;
import me.philcali.rss.api.IArticleRepository;

public class ArticleDynamo implements IArticle {
    private final Item item;
    private final IArticleRepository content;

    public ArticleDynamo(final Item item, final IArticleRepository content) {
        this.item = item;
        this.content = content;
    }

    @Override
    public String getUri() {
        return item.getString("uri");
    }

    @Override
    public String getCommentsUri() {
        return item.getString("commentsUri");
    }

    @Override
    public String getTitle() {
        return item.getString("title");
    }

    @Override
    public String getDescription() {
        return content.get(item.getString("feedId"), getId()).map(IArticle::getDescription).orElse(null);
    }

    @Override
    public String getId() {
        return item.getString("articleId");
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
