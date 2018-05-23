package me.philcali.rss.atom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import me.philcali.rss.api.IArticle;
import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.ISelfDescribing;
import me.philcali.rss.api.model.Article;
import me.philcali.rss.api.model.Feed;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class AtomContentHandler extends DefaultHandler {
    private static final String RSS_FEED_DATE = "EEE, d MMM yyyy HH:mm:ss Z";
    private static final String ATOM_FEED_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    private Stack<StringBuilder> contentBuilders;
    private Stack<Article.Builder> stack;
    private List<IArticle> articles;
    private Feed.Builder builder;
    private IFeed feed;
    
    @Override
    public void startDocument() throws SAXException {
        builder = Feed.builder();
        stack = new Stack<>();
        contentBuilders = new Stack<>();
        articles = new ArrayList<>();
    }
    
    private String getTagName(final String uri, final String localName, final String qName) {
        if (uri.isEmpty()) {
            return qName;
        } else {
            return localName;
        }
    }
    
    private String buildContent(final char[] ch, final int start, final int length) {
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + length; i++) {
            sb.append(ch[i]);
        }
        return sb.toString();
    }
    
    private ISelfDescribing<?> getFeedOrRecentArticleBuilder() {
        return Optional.of(builder)
                .map(b -> (ISelfDescribing) b)
                .filter(b -> stack.isEmpty())
                .orElseGet(() -> (ISelfDescribing) stack.peek());
    }
    
    private Date parseDate(final String dateString, final String format) throws SAXException {
        try {
            final SimpleDateFormat df = new SimpleDateFormat(format);
            return df.parse(dateString);
        } catch (ParseException pe) {
            throw new SAXException(pe);
        }
    }
    
    private void handleSubElements(final String tagName) throws SAXException {
        final String content = contentBuilders.pop().toString();
        if (!content.trim().isEmpty()) {
            final ISelfDescribing<?> describingBuilder = getFeedOrRecentArticleBuilder();
            switch (tagName.toUpperCase()) {
            case "CATEGORY":
                describingBuilder.addCategory(content);
                break;
            case "COMMENTS":
                stack.peek().withCommentsUri(content);
                break;
            case "TITLE":
                describingBuilder.withTitle(content);
                break;
            case "CONTENT":
            case "DESCRIPTION":
                describingBuilder.withDescription(content);
                break;
            case "LASTBUILDDATE":
            case "PUBDATE":
                describingBuilder.withUpdatedAt(parseDate(content, RSS_FEED_DATE));
                break;
            case "UPDATED":
                describingBuilder.withUpdatedAt(parseDate(content, ATOM_FEED_DATE));
                break;
            case "LINK":
                describingBuilder.withUri(content);
                break;
            case "GUID":
            case "ID":
                describingBuilder.withId(content);
                break;
            default:
                describingBuilder.addMetadata(tagName, content);
            }
        }
    }
    
    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        try {
            contentBuilders.peek().append(buildContent(ch, start, length));
        } catch (EmptyStackException nse) {
            throw new SAXException(nse);
        }
    }
    
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
            throws SAXException {
        final String tagUpper = getTagName(uri, localName, qName).toUpperCase();
        switch (tagUpper) {
        case "ENTRY":
        case "ITEM":
            stack.push(Article.builder());
            break;
        case "LINK":
            for (int index = 0; index < attributes.getLength(); index++) {
                final String name = getTagName(
                        attributes.getURI(index),
                        attributes.getLocalName(index),
                        attributes.getQName(index));
                if (name.equals("href")) {
                    getFeedOrRecentArticleBuilder().withUri(attributes.getValue(name));
                }
            }
        default:
            contentBuilders.push(new StringBuilder());
        }
    }
    
    @Override
    public void endElement(final String uri , final String localName, final String qName) throws SAXException {
        final String tagUpper = getTagName(uri, localName, qName).toUpperCase();
        switch (tagUpper) {
        case "ENTRY":
        case "ITEM":
            articles.add(stack.pop().build());
            break;
        default:
            handleSubElements(getTagName(uri, localName, qName));
        }
    }
    
    @Override
    public void endDocument() throws SAXException {
        feed = builder.build();
    }
    
    public IFeed getFeed() {
        return feed;
    }
    
    public List<IArticle> getArticles() {
        return articles;
    }
}
