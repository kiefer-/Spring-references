package me.shouheng.common.captcha;

import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;

/**
 * @author shouh, 2019/4/6-12:59
 */
public class CaptchaImgFont {

    /**
     * 常量，由于太长，放到文件中，服务启动时加载到内存中
     */
    private static String fontStr = null;

    /**
     * 获取用于生成验证码的字体
     *
     * @param fontHeight 字体高度
     * @return 字体
     */
    Font getFont(int fontHeight) {
        try {
            Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(hex2byte(fontStr)));
            return baseFont.deriveFont(Font.PLAIN, fontHeight);
        } catch (Exception e) {
            return new Font("Arial", Font.PLAIN, fontHeight);
        }
    }

    private byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1) {
            return null;
        }

        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 初始化验证码图片字体，可以在 Spring 容器启动的时候调用
     */
    public static void initImgFont() {
        URL fontUrl = CaptchaImgFont.class.getClassLoader().getResource("captcha_font.txt");
        try {
            fontStr = FileUtils.readFileToString(new File(fontUrl.toURI()), "UTF-8");
        } catch (Exception e) {
            fontStr = "";
        }
    }
}
