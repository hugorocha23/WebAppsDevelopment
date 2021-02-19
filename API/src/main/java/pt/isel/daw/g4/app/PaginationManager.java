package pt.isel.daw.g4.app;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Manages all pageable database queries related information
 * @param <T>
 */
public class PaginationManager<T> {

    private PageRequest pageable;
    private Page<T> request;

    private final int PAGE_SIZE = 3;

    public PaginationManager(int currentPage, Sort.Direction sortingType, String... props) {
        pageable = PageRequest.of(currentPage, PAGE_SIZE, sortingType, props);
    }

    public void setRequest(Page<T> request) {
        this.request = request;
    }

    public PageRequest getPageable() {
        return pageable;
    }

    public List<T> getContent() {
        return request.getContent();
    }

    public Page<T> getRequest() {
        return request;
    }
}
