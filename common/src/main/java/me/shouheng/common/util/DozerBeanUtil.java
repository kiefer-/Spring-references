package me.shouheng.common.util;

import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shouh, 2019/3/31-17:36
 */
public class DozerBeanUtil {

    private Mapper mapper;

    public DozerBeanUtil(Mapper mapper) {
        this.mapper = mapper;
    }

    @SuppressWarnings("unchecked")
    public <P> P clone(P base) {
        if (base == null) {
            return null;
        } else {
            return (P) mapper.map(base, base.getClass());
        }
    }

    public <P> List<P> cloneList(List<P> baseList) {
        if (baseList == null) {
            return null;
        } else {
            List<P> targetList = new ArrayList<P>();
            for (P p : baseList) {

                targetList.add((P) clone(p));
            }
            return targetList;
        }
    }

    public <V, P> P convert(V base, P target) {
        if (base != null) {
            mapper.map(base, target);
            return target;
        }
        return target;
    }

    public <V, P> P convert(V base, Class<P> target) {
        if (base == null) {
            return null;
        } else {
            return mapper.map(base, target);
        }
    }

    public <V, P> List<P> convertList(List<V> baseList, Class<P> target) {
        if (baseList == null) {
            return null;
        } else {
            List<P> targetList = new ArrayList<>();
            for (V vo : baseList) {
                targetList.add(convert(vo, target));
            }
            return targetList;
        }
    }
}
