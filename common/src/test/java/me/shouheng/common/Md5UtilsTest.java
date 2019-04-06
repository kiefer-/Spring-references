package me.shouheng.common;

import me.shouheng.common.util.Md5Utils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author shouh, 2019/4/6-11:10
 */
public class Md5UtilsTest {

    @Test
    public void testMd5() {
        Assert.assertEquals(Md5Utils.md5("12345"), "827ccb0eea8a706c4c34a16891f84e7b");
        Assert.assertEquals(Md5Utils.md5("WngShhng"), "3c9976b3e4446b07b522bfc58e1403b0");
        Assert.assertEquals(Md5Utils.md5("Md5Utils"), "62c9a9d868ad82289afa4a4bacbcb4a6");
        Assert.assertEquals(Md5Utils.md5("中文#1234_WWW"), "4b587e82e0286415559640be0cc45875");
        Assert.assertEquals(Md5Utils.md5("中文测试"), "089b4943ea034acfa445d050c7913e55");
    }
}
