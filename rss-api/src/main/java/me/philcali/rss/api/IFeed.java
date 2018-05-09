package me.philcali.rss.api;

import java.util.List;

public interface IFeed {
    List<IFeed> getFeeds();
    String getType();
    String getTitle();
    String getText();
    String getHtmlUrl();
    String getXmlUrl();
}
