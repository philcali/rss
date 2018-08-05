package me.philcali.rss.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.ISelfDescribing;

public class Feed implements IFeed {
    private String description;
    private String title;
    private String uri;
    private String htmlUri;
    private Date updatedAt;
    private String id;
    private List<String> categories;
    private Map<String, String> metadata;

    public static final class Builder implements ISelfDescribing<Feed.Builder> {
        private String description;
        private String title;
        private String uri;
        private String htmlUri;
        private Date updatedAt;
        private String id;
        private List<String> categories;
        private Map<String, String> metadata;

        @Override
        public Builder withId(final String id) {
            this.id = md5(id);
            return this;
        }

        public Builder withMetadata(final Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }

        @Override
        public Builder addMetadata(final String key, final String value) {
            if (Objects.isNull(metadata)) {
                metadata = new HashMap<>();
            }
            metadata.put(key, value);
            return this;
        }

        public Builder withCategories(final List<String> categories) {
            this.categories = categories;
            return this;
        }

        @Override
        public Builder addCategory(final String category) {
            if (Objects.isNull(categories)) {
                categories = new ArrayList<>();
            }
            categories.add(category);
            return this;
        }

        @Override
        public Builder withUpdatedAt(final Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        @Override
        public Builder withUri(final String uri) {
            this.uri = uri;
            return this;
        }

        @Override
        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }

        @Override
        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }

        @Override
        public Builder withHtmlUri(final String htmlUri) {
            this.htmlUri = htmlUri;
            return this;
        }

        public Feed build() {
            Objects.requireNonNull(uri);
            if (Objects.isNull(id)) {
                withId(uri);
            }
            return new Feed(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Feed() {
    }

    private Feed(final Builder builder) {
        setTitle(builder.title);
        setDescription(builder.description);
        setUri(builder.uri);
        setUpdatedAt(builder.updatedAt);
        setId(builder.id);
        setMetadata(builder.metadata);
        setCategories(builder.categories);
        setHtmlUri(builder.htmlUri);
    }

    public void setHtmlUri(final String htmlUri) {
        this.htmlUri = htmlUri;
    }

    public void setCategories(final List<String> categories) {
        this.categories = categories;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setMetadata(final Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public List<String> getCategories() {
        return categories;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getUri() {
        return uri;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Map<String, String> getMetadata() {
        return metadata;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getHtmlUri() {
        return htmlUri;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, uri, htmlUri, updatedAt, metadata);
    }

    @Override
    public boolean equals(final Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof IFeed)) {
            return false;
        }

        final IFeed feed = (IFeed) obj;
        return Objects.equals(id, feed.getId())
                && Objects.equals(title, feed.getTitle())
                && Objects.equals(description, feed.getDescription())
                && Objects.equals(uri, feed.getUri())
                && Objects.equals(htmlUri, feed.getHtmlUri())
                && Objects.equals(updatedAt, feed.getUpdatedAt())
                && Objects.equals(metadata, feed.getMetadata());
    }

    @Override
    public String toString() {
        return "Feed[id=" + id
                + ", title=" + title
                + ", description=" + description
                + ", uri=" + uri
                + ", htmlUri=" + htmlUri
                + ", updatedAt=" + updatedAt
                + ", metadata=" + metadata + "]";
    }
}
