package me.philcali.rss.api;

import java.util.Date;
import java.util.Map;

public interface IFeed {
    String getId();
    String getTitle();
    String getDescription();
    String getUrl();
    Date getUpdatedAt();
    Map<String, String> getMetadata();
}
