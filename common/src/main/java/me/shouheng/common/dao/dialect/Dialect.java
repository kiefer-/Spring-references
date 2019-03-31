package me.shouheng.common.dao.dialect;

/**
 * @author shouh, 2019/3/31-18:16
 */
public abstract class Dialect {

    public abstract boolean supportsLimit();

    public abstract boolean supportsLimitOffset();

    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
    }

    public abstract String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder);

}
