package me.shouheng.common.util;

/**
 * @author shouh 2019/3/30-16:18
 */
public class StringUtils {

    /**
     * If the given text is empty, that is null or length equals 0
     *
     * @param text the original text
     * @return is the text empty or not
     */
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }
}
