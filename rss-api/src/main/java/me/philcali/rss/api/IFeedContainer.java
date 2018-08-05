package me.philcali.rss.api;

import java.util.List;

public interface IFeedContainer {
    IFeed getFeed();

    List<IArticle> getArticles();
}
