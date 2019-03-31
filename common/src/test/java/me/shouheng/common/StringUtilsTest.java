package me.shouheng.common;

import me.shouheng.common.util.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shouh, 2019/3/30-16:20
 */
public class StringUtilsTest {

    private Logger logger = LoggerFactory.getLogger(StringUtilsTest.class);

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
        Assert.assertTrue(StringUtils.isEmpty(""));
    }
}
