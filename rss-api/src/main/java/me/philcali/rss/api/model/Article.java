package me.philcali.rss.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import me.philcali.rss.api.IArticle;
import me.philcali.rss.api.ISelfDescribing;

public class Article implements IArticle {
    private String id;
    private List<String> categories;
    private Map<String, String> metadata;
    private String description;
    private String title;
    private String uri;
    private String commentsUri;
    private Date updatedAt;
    
    public static final class Builder implements ISelfDescribing<Article.Builder> {
        private String id;
        private List<String> categories = new ArrayList<>();
        private Map<String, String> metadata = new HashMap<>();
        private String title;
        private String description;
        private String uri;
        private String commentsUri;
        private Date updatedAt;
        
        @Override
        public Builder withDescription(final String description) {
            this.description = description;
            return this;
        }
        
        @Override
        public Builder withId(final String id) {
            this.id = id;
            return this;
        }
        
        @Override
        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }
        
        @Override
        public Builder withUri(final String uri) {
            this.uri = uri;
            return this;
        }
        
        public Builder withCommentsUri(final String commentsUri) {
            this.commentsUri = commentsUri;
            return this;
        }
        
        public Builder withMetadata(final Map<String, String> metadata) {
            this.metadata = metadata;
            return this;
        }
        
        @Override
        public Builder addMetadata(final String key, final String value) {
            this.metadata.put(key, value);
            return this;
        }
        
        @Override
        public Builder withUpdatedAt(final Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public Builder withCategories(final List<String> categories) {
            this.categories = categories;
            return this;
        }
        
        @Override
        public Builder addCategory(final String category) {
            this.categories.add(category);
            return this;
        }
        
        public Article build() {
            return new Article(this);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public Article() {
    }
    
    private Article(final Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setUri(builder.uri);
        setCommentsUri(builder.commentsUri);
        setUpdatedAt(builder.updatedAt);
        setDescription(builder.description);
        setCategories(builder.categories);
        setMetadata(builder.metadata);
    }
    
    public void setMetadata(final Map<String, String> metadata) {
        this.metadata = metadata;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setUri(final String uri) {
        this.uri = uri;
    }
    
    public void setCommentsUri(final String commentsUri) {
        this.commentsUri = commentsUri;
    }
    
    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setCategories(final List<String> categories) {
        this.categories = categories;
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
    public String getDescription() {
        return description;
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
    public String getUri() {
        return uri;
    }
    
    @Override
    public String getCommentsUri() {
        return commentsUri;
    }
    
    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, uri, commentsUri, categories, metadata, updatedAt);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof IArticle)) {
            return false;
        }
        
        final IArticle article = (IArticle) obj;
        return Objects.equals(id, article.getId())
                && Objects.equals(title, article.getTitle())
                && Objects.equals(description, article.getDescription())
                && Objects.equals(uri, article.getUri())
                && Objects.equals(commentsUri, article.getCommentsUri())
                && Objects.equals(categories, article.getCategories())
                && Objects.equals(metadata, article.getMetadata())
                && Objects.equals(updatedAt, article.getUpdatedAt());
    }
    
    @Override
    public String toString() {
        return "Article[id=" + id
                + ", title=" + title
                + ", description=" + description
                + ", uri=" + uri
                + ", commentsUri=" + commentsUri 
                + ", updatedAt=" + updatedAt
                + ", categories=" + categories
                + ", metadata=" + metadata + "]";
    }
}
