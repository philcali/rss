package me.philcali.rss.api.me.philcali.rss.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import me.philcali.db.api.ICondition;
import me.philcali.db.api.QueryParams;
import me.philcali.db.api.QueryResult;
import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.IFeedCollector;
import me.philcali.rss.api.IFeedExport;
import me.philcali.rss.api.IFeedRepository;

public class FeedExport implements IFeedExport, IFeedRepository {
    private String title;
    private List<IFeed> feeds;
    
    public static class Builder implements IFeedCollector<FeedExport.Builder> {
        private String title;
        private List<IFeed> feeds = new ArrayList<>();
        
        public Builder withTitle(final String title) {
            this.title = title;
            return this;
        }
        
        @Override
        public Builder withFeeds(final IFeed ... feeds) {
            this.feeds.addAll(Arrays.asList(feeds));
            return this;
        }
        
        public Builder withFeeds(final List<IFeed> feeds) {
            this.feeds = feeds;
            return this;
        }
        
        public IFeedExport build() {
            return new FeedExport(this);
        }
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public FeedExport() {
    }
    
    private FeedExport(final Builder builder) {
        this.title = builder.title;
        this.feeds = builder.feeds;
    }

    @Override
    public String getTitle() {
        return title;
    }
    
    @Override
    public List<IFeed> getFeeds() {
        return feeds;
    }

    @Override
    public QueryResult<IFeed> list(final QueryParams params) {
        final Predicate<IFeed> filteringFunc = filterFeed(params);
        final List<IFeed> filteredFeeds = Optional.ofNullable(getFeeds())
                .map(fs -> fs.stream().filter(filteringFunc).collect(Collectors.toList()))
                .orElseGet(Collections::emptyList);
        return new QueryResult<>(Optional.empty(), filteredFeeds, false);
    }
    
    private Predicate<IFeed> filterFeed(final QueryParams params) {
        final Predicate<IFeed> identity = feed -> true;
        return params.getConditions().values().stream()
                .reduce(identity, (left, right) -> left.and(testCondition(right)), (left, right) -> left);
    }
    
    private String getFeedValue(final IFeed feed, final String attribute) {
        switch (attribute) {
        case "xmlUrl":
            return feed.getXmlUrl();
        case "htmlUrl":
            return feed.getHtmlUrl();
        default:
            return feed.getTitle();
        }
    }
    
    private Predicate<IFeed> testCondition(final ICondition condition) {
        return feed -> {
            final String value = getFeedValue(feed, condition.getAttribute());
            switch (condition.getComparator()) {
            case EQUALS:
                return value.equals(condition.getValue());
            case NOT_EQUALS:
                return !value.equals(condition.getValue());
            case STARTS_WITH:
                return value.startsWith(condition.getValue().toString());
            case CONTAINS:
                return value.contains(condition.getValue().toString());
            case IN:
                return Arrays.stream(condition.getValues())
                    .map(Object::toString)
                    .anyMatch(value::equals);
            default:
                return false;
            }
        };
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (Objects.isNull(obj) || !(obj instanceof IFeedExport)) {
            return false;
        }
        
        final IFeedExport export = (IFeedExport) obj;
        return Objects.equals(title, export.getTitle()) && Objects.equals(feeds, export.getFeeds());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, feeds);
    }
    
    @Override
    public String toString() {
        return "FeedExport [title=" + title + ", feeds=" + feeds + "]";
    }
}
