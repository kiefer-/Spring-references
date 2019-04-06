package me.shouheng.common;

import me.shouheng.common.captcha.CaptchaImgFont;

/**
 * @author shouh, 2019/4/6-12:31
 */
public class DataInitializer {

    /**
     * 如果工具库希望在 Spring 容器启动的时候初始化数据，可以在这个方法中调用
     */
    public static void init() {
        CaptchaImgFont.initImgFont();
    }
}
