package me.philcali.rss.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IFeed {
    String getId();
    String getTitle();
    String getDescription();
    String getUri();
    String getHtmlUri();
    Date getUpdatedAt();
    List<String> getCategories();
    Map<String, String> getMetadata();
}
