package me.shouheng.common.model.query;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author shouh, 2019/3/31-15:35
 */
public class SearchObject implements Pageable, Sortable, Serializable {

    private static final long serialVersionUID = 4009650343975989289L;

    private int currentPage;

    private int pageSize;

    private List<Sort> sorts = new LinkedList<>();

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public List<Sort> getSorts() {
        return sorts;
    }

    @Override
    public void addSort(Sort sort) {
        sorts.add(sort);
    }
}
