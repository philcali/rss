package me.philcali.rss.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IArticle {
    String getUri();
    String getCommentsUri();
    String getTitle();
    String getDescription();
    String getId();
    Date getUpdatedAt();
    List<String> getCategories();
    Map<String, String> getMetadata();
}
