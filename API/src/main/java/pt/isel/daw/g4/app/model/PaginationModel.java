package pt.isel.daw.g4.app.model;

import org.springframework.data.domain.Page;
import pt.isel.daw.g4.app.PaginationManager;
import pt.isel.daw.g4.app.model.collection_json.CollectionJsonSchema;

import javax.servlet.http.HttpServletResponse;

public class PaginationModel {

    public PaginationManager manager;

    public CollectionJsonSchema data;

    private final String QUERY_STRING_TEMPLATE = "<%s?page=%s>; rel=\"%s\"";

    public PaginationModel(PaginationManager manager, CollectionJsonSchema data) {
        this.manager = manager;
        this.data = data;
    }

    public void setupPagingHeaders(HttpServletResponse res, String url, int currentPage) {
        Page<CollectionJsonSchema> request = manager.getRequest();
        StringBuilder linkHeader = new StringBuilder();

        if(request.hasNext()) {
            linkHeader.append(String.format(QUERY_STRING_TEMPLATE, url, currentPage + 1, "next"));
        }

        if(request.hasPrevious()) {
            if(linkHeader.length() != 0) {
                linkHeader.append(",");
            }

            linkHeader.append(String.format(QUERY_STRING_TEMPLATE, url, currentPage - 1, "prev"));
        }

        res.addHeader("Link", linkHeader.toString());
    }
}
