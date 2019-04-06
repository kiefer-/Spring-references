package me.shouheng.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author shouh, 2019/4/6-11:06
 */
public class Md5Utils {

    private static final Logger logger = LoggerFactory.getLogger(Md5Utils.class);

    public static String md5(final String s) {
        return md5(s.getBytes());
    }

    public static String md5(byte[] bytes) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(bytes);
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2) {
                    h.insert(0, "0");
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error when generating md5 {} ", e);
        }
        return null;
    }
}
