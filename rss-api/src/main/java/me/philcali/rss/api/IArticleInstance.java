package me.philcali.rss.api;

import java.util.Date;

public interface IArticleInstance {
    String getArticleId();
    String getFeedId();
    String getUserId();
    Date getPublicationDate();
    Date getReadDate();
    Date getArchivedDate();
    boolean isFlagged();
    boolean isRead();
    boolean isArchived();
}
