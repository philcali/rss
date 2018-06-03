package me.philcali.rss.opml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import me.philcali.rss.api.ompl.DocumentCreationException;
import me.philcali.rss.api.ompl.IDocument;
import me.philcali.rss.api.ompl.IDocumentFactory;
import me.philcali.rss.api.ompl.model.Document;

public class DocumentFactoryImpl implements IDocumentFactory {
    private final SAXParserFactory factory;
    
    public DocumentFactoryImpl(final SAXParserFactory factory) {
        this.factory = factory;
    }
    
    public DocumentFactoryImpl() {
        this(SAXParserFactory.newInstance());
    }
    
    @Override
    public IDocument create(final InputStream input) throws DocumentCreationException {
        try {
            final XMLReader reader = factory.newSAXParser().getXMLReader();
            final Document.Builder builder = Document.builder();
            reader.setContentHandler(new OPMLContentHandler(builder));
            reader.parse(new InputSource(input));
            return builder.build();
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            throw new DocumentCreationException(ex);
        }

    }
}
