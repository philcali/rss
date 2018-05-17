package me.philcali.rss.api;

import java.util.Date;
import java.util.List;

public interface IArticle {
    String getUri();
    String getCommentsUri();
    String getTitle();
    String getContent();
    String getId();
    String getCreator();
    Date getPublicationDate();
    List<String> getCategories();
}
