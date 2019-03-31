package me.shouheng.common.model.query;

import java.util.List;

/**
 * @author shouh, 2019/3/31-15:33
 */
public interface Sortable {

    List<Sort> getSorts();

    void addSort(Sort sort);
}
