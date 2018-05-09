package me.philcali.rss.api;

import java.util.List;

public interface IFeedExport {
    String getTitle();
    List<IFeed> getFeeds();
}
