package me.philcali.rss.api.ompl.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import me.philcali.rss.api.ompl.IDocument;
import me.philcali.rss.api.ompl.IOutline;
import me.philcali.rss.api.ompl.IOutlineCollector;

public class Document implements IDocument {
    private String title;
    private String version;
    private List<IOutline> outlines;
    
    public static class Builder implements IOutlineCollector<Document.Builder> {
        private String title;
        private String version;
        private List<IOutline> outlines = new ArrayList<>();
        
        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }
        
        public Builder withVersion(final String version) {
            this.version = version;
            return this;
        }
        
        @Override
        public Builder withOutlines(final IOutline ... outlines) {
            this.outlines.addAll(Arrays.asList(outlines));
            return this;
        }
        
        public IDocument build() {
            return new Document(this);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public Document() {
    }
    
    private Document(final Builder builder) {
        this.title = builder.title;
        this.outlines = builder.outlines;
        this.version = builder.version;
    }

    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public List<IOutline> getOutlines() {
        return outlines;
    }
    
    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean equals(final Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof IDocument)) {
            return false;
        }
        
        final IDocument export = (IDocument) obj;
        return Objects.equals(title, export.getTitle())
                && Objects.equals(version, export.getVersion())
                && Objects.equals(outlines, export.getOutlines());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, version);
    }
    
    @Override
    public String toString() {
        return "Document[title=" + title + ", version=" + version + ", outlines=" + outlines + "]";
    }
}
