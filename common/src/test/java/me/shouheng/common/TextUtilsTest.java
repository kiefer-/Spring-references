package me.shouheng.common;

import me.shouheng.common.util.TextUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shouh, 2019/3/30-16:20
 */
public class TextUtilsTest {

    private Logger logger = LoggerFactory.getLogger(TextUtilsTest.class);

    @Before
    public void before() {
        logger.debug("----before");
    }

    @After
    public void after() {
        logger.debug("----after");
    }

    @Test
    public void testEmpty() {
        Assert.assertTrue(TextUtils.isEmpty(""));
    }
}
