package me.philcali.rss.service.model;

import java.util.List;

import me.philcali.db.api.IPageKey;
import me.philcali.db.api.QueryResult;
import me.philcali.service.annotations.request.Body;
import me.philcali.service.annotations.request.HeaderParam;
import me.philcali.service.annotations.request.ParamFilter;
import me.philcali.service.reflection.filter.Base64Encode;

public class ListResourceResponse<T> {
    @Body
    private final List<T> items;

    @ParamFilter(Base64Encode.class)
    @HeaderParam("X-Next-Token")
    private final IPageKey nextToken;

    public ListResourceResponse(final QueryResult<T> results) {
        this.items = results.getItems();
        this.nextToken = results.getToken();
    }

    public List<T> getItems() {
        return items;
    }

    public IPageKey getNextToken() {
        return nextToken;
    }
}
