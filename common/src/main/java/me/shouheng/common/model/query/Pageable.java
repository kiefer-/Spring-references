package me.shouheng.common.model.query;

/**
 * @author shouh, 2019/3/31-15:34
 */
public interface Pageable {

    int getCurrentPage();

    void setCurrentPage(int currentPage);

    int getPageSize();

    void setPageSize(int pageSize);
}
