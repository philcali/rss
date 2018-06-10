 package me.philcali.rss.service;

import javax.inject.Inject;

import me.philcali.rss.api.IFeedRepository;
import me.philcali.rss.api.ompl.IDocument;
import me.philcali.rss.api.ompl.IDocumentFactory;
import me.philcali.service.annotations.POST;
import me.philcali.service.annotations.request.Authorizer;
import me.philcali.service.annotations.request.FormParam;

public class OPMLResource {
    private final IDocumentFactory factory;
    private final IFeedRepository feeds;
    
    @Inject
    public OPMLResource(
            final IDocumentFactory factory,
            final IFeedRepository feeds) {
        this.factory = factory;
        this.feeds = feeds;
    }
    
    @POST("/opml/import")
    @Authorizer(ServiceAuthorizer.class)
    public IDocument omplImport(@FormParam("document") final String contents) {
        return factory.create(contents);
    }
}
