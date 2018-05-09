package me.philcali.rss.opml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.philcali.rss.api.IFeedExport;
import me.philcali.rss.api.me.philcali.rss.api.model.Feed;
import me.philcali.rss.api.me.philcali.rss.api.model.FeedExport;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import static org.junit.Assert.assertEquals;

public class OPMLContentHandlerTest {
    private FeedExport.Builder builder;
    private SAXParserFactory spf;
    
    @Before
    public void setUp() {
        spf = SAXParserFactory.newInstance();
        builder = FeedExport.builder();
    }
    
    @Test
    public void testParse() throws Exception {
        SAXParser parser = spf.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(new OPMLContentHandler(builder));
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("test.xml")));
        
        final IFeedExport expectedFeed = FeedExport.builder()
                .withTitle("My Test Feed")
                .withVersion("1.0")
                .withFeeds(Feed.builder()
                        .withText("Outline1 text")
                        .withTitle("Outline1 title")
                        .withFeeds(Feed.builder()
                                .withText("Child1 text")
                                .withXmlUrl("https://example.com/xml")
                                .build())
                        .build(),
                        Feed.builder()
                        .withText("Outline2 text")
                        .withTitle("Outline2 title")
                        .withFeeds(Feed.builder()
                                .withText("Child1 text")
                                .withHtmlUrl("https://example.com/html")
                                .build())
                        .build())
                .build();
        assertEquals(expectedFeed, builder.build());
    }
}
