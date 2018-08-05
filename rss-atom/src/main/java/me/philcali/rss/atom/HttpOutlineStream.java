package me.philcali.rss.atom;

import java.io.InputStream;

import me.philcali.http.api.HttpMethod;
import me.philcali.http.api.IHttpClient;
import me.philcali.http.api.IRequest;
import me.philcali.http.api.IResponse;
import me.philcali.http.api.exception.HttpException;
import me.philcali.rss.api.IOutlineStream;
import me.philcali.rss.api.exception.OutlineStreamException;

public class HttpOutlineStream implements IOutlineStream {
    private static final int BAD_REQUEST_OR_FAILURE = 400;
    private static final int MOVED_PERMANENTLY = 301;
    private static final int MAX_FOLLOWS = 5;
    private static final String LOCATION = "Location";
    private final IHttpClient client;

    public HttpOutlineStream(final IHttpClient client) {
        this.client = client;
    }

    @Override
    public InputStream apply(final String xmlUrl) throws OutlineStreamException {
        try {
            return follows(xmlUrl, 1).body();
        } catch (HttpException he) {
            throw new OutlineStreamException(he);
        }
    }

    private IResponse follows(final String xmlUrl, final int attempt) {
        final IRequest request = client.createRequest(HttpMethod.GET, xmlUrl);
        try {
            final IResponse response = request.respond();
            if (response.status() >= BAD_REQUEST_OR_FAILURE) {
                throw new HttpException(new RuntimeException("Failed to pull " + xmlUrl));
            } else if (response.status() == MOVED_PERMANENTLY) {
                if (attempt < MAX_FOLLOWS) {
                    return follows(response.header(LOCATION), attempt + 1);
                } else {
                    throw new HttpException(new RuntimeException("Max follows " + xmlUrl));
                }
            }
            return response;
        } catch (HttpException he) {
            throw new OutlineStreamException(he);
        }
    }
}
