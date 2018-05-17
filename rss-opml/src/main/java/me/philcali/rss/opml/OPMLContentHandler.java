package me.philcali.rss.opml;

import java.util.Optional;
import java.util.Stack;

import me.philcali.rss.api.ompl.IOutlineCollector;
import me.philcali.rss.api.ompl.model.Document;
import me.philcali.rss.api.ompl.model.Outline;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OPMLContentHandler extends DefaultHandler {
    private final Stack<IOutlineCollector<?>> collectors;
    private final Stack<String> elements;
    private final Document.Builder builder;
    
    public OPMLContentHandler(final Document.Builder builder) {
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
            final Outline.Builder feedBuilder = Outline.builder();
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
            final Outline.Builder collector = (Outline.Builder) collectors.pop();
            collectors.peek().withOutlines(collector.build());
        }
    }
}
