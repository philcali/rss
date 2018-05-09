package me.philcali.rss.api.me.philcali.rss.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.IFeedCollector;

public class Feed implements IFeed {
    private String title;
    private String text;
    private String xmlUrl;
    private String htmlUrl;
    private String type;
    private List<IFeed> feeds;

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public void setHtmlUrl(final String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setXmlUrl(final String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }

    public void setFeeds(final List<IFeed> feeds) {
        this.feeds = feeds;
    }
    
    public static class Builder implements IFeedCollector<Feed.Builder> {
        private String title;
        private String text;
        private String type;
        private String xmlUrl;
        private String htmlUrl;
        private List<IFeed> feeds = new ArrayList<>();
        
        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }
        
        public Builder withText(final String text) {
            this.text = text;
            return this;
        }
        
        public Builder withType(final String type) {
            this.type = type;
            return this;
        }
        
        public Builder withXmlUrl(final String xmlUrl) {
            this.xmlUrl = xmlUrl;
            return this;
        }
        
        public Builder withHtmlUrl(final String htmlUrl) {
            this.htmlUrl = htmlUrl;
            return this;
        }
        
        @Override
        public Builder withFeeds(final IFeed ... feeds) {
            this.feeds.addAll(Arrays.asList(feeds));
            return this;
        }
        
        public IFeed build() {
            return new Feed(this);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public Feed() {
    }
    
    private Feed(final Builder builder) {
        setFeeds(builder.feeds);
        setHtmlUrl(builder.htmlUrl);
        setXmlUrl(builder.xmlUrl);
        setText(builder.text);
        setTitle(builder.title);
        setType(builder.type);
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getHtmlUrl() {
        return htmlUrl;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getXmlUrl() {
        return xmlUrl;
    }

    @Override
    public List<IFeed> getFeeds() {
        return feeds;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof IFeed)) {
            return false;
        }
        
        final IFeed other = (IFeed) obj;
        return Objects.equals(text, other.getText())
                && Objects.equals(title, other.getTitle())
                && Objects.equals(type, other.getType())
                && Objects.equals(xmlUrl, other.getXmlUrl())
                && Objects.equals(htmlUrl, other.getHtmlUrl())
                && Objects.equals(feeds, other.getFeeds());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(text, title, type, xmlUrl, htmlUrl, feeds);
    }
    
    @Override
    public String toString() {
        return "Feed[title=" + title 
                + ", text=" + text
                + ", type=" + type
                + ", xmlUrl=" + xmlUrl
                + ", htmlUrl=" + htmlUrl
                + ", feeds=" + feeds + "]";
    }
}
