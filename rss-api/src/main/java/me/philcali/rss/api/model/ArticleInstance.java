package me.philcali.rss.api.model;

import java.util.Date;

import me.philcali.rss.api.IArticleInstance;

public class ArticleInstance implements IArticleInstance {
    public static final class Builder {
        private String articleId;
        private String feedId;
        private String userId;
        private Date publicationDate;
        private Date readDate;
        private Date archivedDate;
        private boolean flagged;
        private boolean read;
        private boolean archived;

        public Builder withArticleId(final String articleId) {
            this.articleId = articleId;
            return this;
        }

        public Builder withFeedId(final String feedId) {
            this.feedId = feedId;
            return this;
        }

        public Builder withUserId(final String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withReadDate(final Date readDate) {
            this.readDate = readDate;
            return this;
        }

        public Builder withPublicationDate(final Date publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder withArchivedDate(final Date archivedDate) {
            this.archivedDate = archivedDate;
            return this;
        }

        public Builder withRead(final boolean read) {
            this.read = read;
            return this;
        }

        public Builder withArchived(final boolean archived) {
            this.archived = archived;
            return this;
        }

        public Builder withFlagged(final boolean flagged) {
            this.flagged = flagged;
            return this;
        }

        public ArticleInstance build() {
            return new ArticleInstance(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private String articleId;
    private String feedId;
    private String userId;
    private Date publicationDate;
    private Date readDate;
    private Date archivedDate;
    private boolean flagged;
    private boolean archived;
    private boolean read;

    public ArticleInstance() {
    }

    public ArticleInstance(final Builder builder) {
        this.articleId = builder.articleId;
        this.feedId = builder.feedId;
        this.userId = builder.userId;
        this.read = builder.read;
        this.archived = builder.archived;
        this.flagged = builder.flagged;
        this.publicationDate = builder.publicationDate;
        this.archivedDate = builder.archivedDate;
        this.readDate = builder.readDate;
    }

    @Override
    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(final String articleId) {
        this.articleId = articleId;
    }

    @Override
    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(final String feedId) {
        this.feedId = feedId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    @Override
    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(final Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(final Date readDate) {
        this.readDate = readDate;
    }

    @Override
    public Date getArchivedDate() {
        return archivedDate;
    }

    public void setArchivedDate(final Date archivedDate) {
        this.archivedDate = archivedDate;
    }

    @Override
    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(final boolean flagged) {
        this.flagged = flagged;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    public void setRead(final boolean read) {
        this.read = read;
    }

    @Override
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(final boolean archived) {
        this.archived = archived;
    }
}
