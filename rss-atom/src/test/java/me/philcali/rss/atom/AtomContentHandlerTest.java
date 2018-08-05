package me.philcali.rss.atom;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import me.philcali.rss.api.IArticle;
import me.philcali.rss.api.IFeed;
import me.philcali.rss.api.model.Article;
import me.philcali.rss.api.model.Feed;

public class AtomContentHandlerTest {
    private AtomContentHandler handler;
    private SAXParserFactory factory;
    private SAXParser parser;


    @Before
    public void setUp() throws Exception {
        handler = new AtomContentHandler();
        factory = SAXParserFactory.newInstance();
        parser = factory.newSAXParser();
    }

    @Test
    public void testWellFormedAtom() throws Exception {
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("well_formed_atom.xml")));

        SimpleDateFormat atomFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date expectedFeedDate = atomFormat.parse("2018-05-19T00:00:00Z");
        IFeed expected = Feed.builder()
                .withId("tag:dilbert.com,2005:/feed")
                .withTitle("Dilbert Daily Strip")
                .withHtmlUri("http://dilbert.com")
                .withUri("http://feeds.feedburner.com/DilbertDailyStrip")
                .withUpdatedAt(expectedFeedDate)
                .build();

        List<IArticle> expectedArticles = Arrays.asList(
            Article.builder()
                    .withId("2018-05-19")
                    .withTitle("Comic for May 19, 2018")
                    .withDescription("Dilbert readers - Please visit Dilbert.com to read this feature. Due to changes with our feeds, we are now making this RSS feed a link to Dilbert.com.<img src=\"http://feeds.feedburner.com/~r/DilbertDailyStrip/~4/JpaUSAnbxDY\" height=\"1\" width=\"1\" alt=\"\"/>")
                    .withUri("http://feedproxy.google.com/~r/DilbertDailyStrip/~3/JpaUSAnbxDY/2018-05-19")
                    .withUpdatedAt(atomFormat.parse("2018-05-19T23:59:59+00:00"))
                    .addMetadata("feedburner:origLink", "http://dilbert.com/strip/2018-05-19")
                    .build(),
            Article.builder()
                    .withId("2018-05-18")
                    .withTitle("Comic for May 18, 2018")
                    .withDescription("Dilbert readers - Please visit Dilbert.com to read this feature. Due to changes with our feeds, we are now making this RSS feed a link to Dilbert.com.<img src=\"http://feeds.feedburner.com/~r/DilbertDailyStrip/~4/Lu08hziyr44\" height=\"1\" width=\"1\" alt=\"\"/>")
                    .withUri("http://feedproxy.google.com/~r/DilbertDailyStrip/~3/Lu08hziyr44/2018-05-18")
                    .withUpdatedAt(atomFormat.parse("2018-05-18T23:59:59+00:00"))
                    .addMetadata("feedburner:origLink", "http://dilbert.com/strip/2018-05-18")
                    .build()
        );
        assertEquals(expected, handler.getFeed());
        assertEquals(expectedArticles, handler.getArticles());
    }

    @Test
    public void testWellFormedRss() throws Exception {
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("well_formed_rss.xml")));

        Date expectedFeedDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse("Wed, 16 May 2018 18:53:46 +0000");
        IFeed expected = Feed.builder()
                .withTitle("AWS DevOps Blog")
                .withHtmlUri("https://aws.amazon.com/blogs/devops/")
                .withUri("https://aws.amazon.com/blogs/devops/feed/")
                .withUpdatedAt(expectedFeedDate)
                .addMetadata("language", "en-US")
                .addMetadata("sy:updatePeriod", "hourly")
                .addMetadata("sy:updateFrequency", "1")
                .build();
        assertEquals(expected, handler.getFeed());
    }

    @Test
    public void testAtomExtensions() throws Exception {
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("well_formed_rss.xml")));

        parser = factory.newSAXParser();
        reader = parser.getXMLReader();
        AtomContentHandler handler2 = new AtomContentHandler().extend(new ContentEncodedExtension());
        reader.setContentHandler(handler2);
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("well_formed_rss.xml")));

        List<IArticle> handlerArticles = handler.getArticles();
        List<IArticle> handler2Articles = handler2.getArticles();

        assertEquals(handlerArticles.size(), handler2Articles.size());
        for (int i = 0; i < handlerArticles.size(); i++) {
            assertEquals(handlerArticles.get(i).getMetadata().get("content:encoded"), handler2Articles.get(i).getDescription());
        }
    }

    @Test
    public void testFeedburnerLinkExtension() throws Exception {
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(handler);
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("well_formed_atom.xml")));

        parser = factory.newSAXParser();
        reader = parser.getXMLReader();
        AtomContentHandler handler2 = new AtomContentHandler().extend(new FeedburnerOrigLinkExtension());
        reader.setContentHandler(handler2);
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("well_formed_atom.xml")));

        List<IArticle> handlerArticles = handler.getArticles();
        List<IArticle> handler2Articles = handler2.getArticles();

        assertEquals(handlerArticles.size(), handler2Articles.size());
        for (int i = 0; i < handlerArticles.size(); i++) {
            assertEquals(handlerArticles.get(i).getMetadata().get("feedburner:origLink"), handler2Articles.get(i).getUri());
        }
    }
}
