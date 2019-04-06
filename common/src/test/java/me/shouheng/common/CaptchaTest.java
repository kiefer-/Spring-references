package me.shouheng.common;

import me.shouheng.common.captcha.Captcha;
import org.junit.Test;

import java.io.IOException;

/**
 * @author shouh, 2019/4/6-12:56
 */
public class CaptchaTest {

    @Test
    public void generateCaptcha() {
        Captcha captcha = new Captcha.Builder().build();
        try {
            String path = System.currentTimeMillis() + ".png";
            System.out.println(captcha.getCode() + " >" + path);
            captcha.write(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
