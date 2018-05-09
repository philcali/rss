package me.philcali.rss.opml;

import java.util.Optional;
import java.util.Stack;

import me.philcali.rss.api.IFeedCollector;
import me.philcali.rss.api.me.philcali.rss.api.model.Feed;
import me.philcali.rss.api.me.philcali.rss.api.model.FeedExport;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OPMLContentHandler extends DefaultHandler {
    private final Stack<IFeedCollector<?>> collectors;
    private final Stack<String> elements;
    private final FeedExport.Builder builder;
    
    public OPMLContentHandler(final FeedExport.Builder builder) {
        this.builder = builder;
        this.collectors = new Stack<>();
        this.elements = new Stack<>();
        collectors.push(builder);
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!elements.isEmpty()) {
            switch(elements.pop()) {
            case "TITLE":
                final StringBuilder sb = new StringBuilder();
                for (int i = start; i < start + length; i++) {
                    sb.append(ch[i]);
                }
                builder.withTitle(sb.toString());
                break;
            }
        }
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        final String tagUpper = qName.toUpperCase();
        elements.push(tagUpper);
        switch (tagUpper) {
        case "OPML":
            Optional.ofNullable(attributes.getValue(0)).ifPresent(builder::withVersion);
            break;
        case "OUTLINE":
            final Feed.Builder feedBuilder = Feed.builder();
            for (int i = 0; i < attributes.getLength(); i++) {
                final String value = attributes.getValue(i);
                switch (attributes.getQName(i)) {
                case "title":
                    feedBuilder.withTitle(value);
                    break;
                case "text":
                    feedBuilder.withText(value);
                    break;
                case "type":
                    feedBuilder.withType(value);
                    break;
                case "xmlUrl":
                    feedBuilder.withXmlUrl(value);
                    break;
                case "htmlUrl":
                    feedBuilder.withHtmlUrl(value);
                    break;
                }
            }
            collectors.push(feedBuilder);
            break;
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName.toUpperCase()) {
        case "OUTLINE":
            final Feed.Builder collector = (Feed.Builder) collectors.pop();
            collectors.peek().withFeeds(collector.build());
        }
    }
}
