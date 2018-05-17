package me.philcali.rss.api.ompl.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.philcali.rss.api.ompl.IOutline;
import me.philcali.rss.api.ompl.IOutlineCollector;

public class Outline implements IOutline {
    private String title;
    private String text;
    private String xmlUrl;
    private String htmlUrl;
    private String type;
    private List<IOutline> outlines;
    
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

    public void setOutlines(final List<IOutline> outlines) {
        this.outlines = outlines;
    }
    
    public static class Builder implements IOutlineCollector<Outline.Builder> {
        private String title;
        private String text;
        private String type;
        private String xmlUrl;
        private String htmlUrl;
        private List<IOutline> outlines = new ArrayList<>();
        
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
        public Builder withOutlines(final IOutline... feeds) {
            this.outlines.addAll(Arrays.asList(feeds));
            return this;
        }
        
        public IOutline build() {
            return new Outline(this);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public Outline() {
    }
    
    private Outline(final Builder builder) {
        setOutlines(builder.outlines);
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
    public List<IOutline> getOutlines() {
        return outlines;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof IOutline)) {
            return false;
        }
        
        final IOutline other = (IOutline) obj;
        return Objects.equals(text, other.getText())
                && Objects.equals(title, other.getTitle())
                && Objects.equals(type, other.getType())
                && Objects.equals(xmlUrl, other.getXmlUrl())
                && Objects.equals(htmlUrl, other.getHtmlUrl())
                && Objects.equals(outlines, other.getOutlines());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(text, title, type, xmlUrl, htmlUrl, outlines);
    }
    
    @Override
    public String toString() {
        return "Outline[title=" + title 
                + ", text=" + text
                + ", type=" + type
                + ", xmlUrl=" + xmlUrl
                + ", htmlUrl=" + htmlUrl
                + ", feeds=" + outlines + "]";
    }
}
