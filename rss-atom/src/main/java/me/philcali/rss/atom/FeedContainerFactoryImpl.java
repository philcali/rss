package me.philcali.rss.atom;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import me.philcali.rss.api.IFeedContainer;
import me.philcali.rss.api.IFeedContainerFactory;
import me.philcali.rss.api.exception.FeedContainerCreationException;

public class FeedContainerFactoryImpl implements IFeedContainerFactory {
    private final SAXParserFactory factory;
    private final Supplier<AtomContentHandler> handlerFactory;

    public FeedContainerFactoryImpl(
            final SAXParserFactory factory,
            final Supplier<AtomContentHandler> handlerFactory) {
        this.factory = factory;
        this.handlerFactory = handlerFactory;
    }

    public FeedContainerFactoryImpl() {
        this(SAXParserFactory.newInstance(), AtomContentHandler::new);
    }

    @Override
    public IFeedContainer create(final InputStream stream) throws FeedContainerCreationException {
        try {
            final SAXParser parser = factory.newSAXParser();
            final XMLReader reader = parser.getXMLReader();
            final AtomContentHandler contentHandler = handlerFactory.get();
            reader.setContentHandler(contentHandler);
            reader.parse(new InputSource(stream));
            return contentHandler;
        } catch (IOException | SAXException | ParserConfigurationException ie) {
            throw new FeedContainerCreationException(ie);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                throw new FeedContainerCreationException(e);
            }
        }
    }
}
