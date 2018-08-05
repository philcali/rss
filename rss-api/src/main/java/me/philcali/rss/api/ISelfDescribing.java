package me.philcali.rss.api;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public interface ISelfDescribing<T extends ISelfDescribing<T>> {
    T withDescription(String description);
    T withTitle(String title);
    T withUri(String uri);
    T withHtmlUri(String htmlUri);
    T withUpdatedAt(Date updatedAt);
    T withId(String id);
    T addCategory(String category);
    T addMetadata(String key, String value);

    default String md5(final String content) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(content.getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, md5.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
