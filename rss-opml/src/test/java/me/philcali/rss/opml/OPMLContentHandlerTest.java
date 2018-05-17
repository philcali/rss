package me.philcali.rss.opml;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import me.philcali.rss.api.ompl.IDocument;
import me.philcali.rss.api.ompl.model.Document;
import me.philcali.rss.api.ompl.model.Outline;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import static org.junit.Assert.assertEquals;

public class OPMLContentHandlerTest {
    private Document.Builder builder;
    private SAXParserFactory spf;
    
    @Before
    public void setUp() {
        spf = SAXParserFactory.newInstance();
        builder = Document.builder();
    }
    
    @Test
    public void testParse() throws Exception {
        SAXParser parser = spf.newSAXParser();
        XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(new OPMLContentHandler(builder));
        reader.parse(new InputSource(getClass().getClassLoader().getResourceAsStream("test.xml")));
        
        final IDocument expectedDocument = Document.builder()
                .withTitle("My Test Feed")
                .withVersion("1.0")
                .withOutlines(Outline.builder()
                        .withText("Outline1 text")
                        .withTitle("Outline1 title")
                        .withOutlines(Outline.builder()
                                .withText("Child1 text")
                                .withXmlUrl("https://example.com/xml")
                                .build())
                        .build(),
                        Outline.builder()
                        .withText("Outline2 text")
                        .withTitle("Outline2 title")
                        .withOutlines(Outline.builder()
                                .withText("Child1 text")
                                .withHtmlUrl("https://example.com/html")
                                .build())
                        .build())
                .build();
        assertEquals(expectedDocument, builder.build());
    }
}
