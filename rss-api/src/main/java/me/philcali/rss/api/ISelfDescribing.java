package me.philcali.rss.api;

import java.util.Date;

public interface ISelfDescribing<T extends ISelfDescribing<T>> {
    T withDescription(String description);
    T withTitle(String title);
    T withUri(String uri);
    T withUpdatedAt(Date updatedAt);
    T withId(String id);
    T addCategory(String category);
    T addMetadata(String key, String value);
}
