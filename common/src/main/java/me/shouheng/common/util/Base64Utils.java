package me.shouheng.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * @author shouh, 2019/4/6-11:16
 */
public class Base64Utils {

    public static byte[] encodeByte(final byte[] binaryData) {
        return Base64.encodeBase64(binaryData, false);
    }

    public static String encode(final byte[] binaryData) {
        return Base64.encodeBase64String(binaryData);
    }

    public static byte[] decode(final String base64String) {
        return new Base64().decode(base64String);
    }

    public static byte[] decode(final byte[] base64Data) {
        return new Base64().decode(base64Data);
    }

    public static String decodeStr(final String base64String) {
        return new String(decode(base64String));
    }
}
