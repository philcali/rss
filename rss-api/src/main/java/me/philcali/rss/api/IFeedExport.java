package me.philcali.rss.api;

import java.util.List;

public interface IFeedExport {
    String getTitle();
    String getVersion();
    List<IFeed> getFeeds();
}
