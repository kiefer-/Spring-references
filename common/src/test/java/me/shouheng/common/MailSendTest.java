package me.shouheng.common;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试邮件收发
 *
 * @author shouh, 2019/3/30-17:42
 */
public class MailSendTest {

    private static final Logger logger = LoggerFactory.getLogger(MailSendTest.class);

    @Before
    public void prepare() {
    }

    @Test
    public void sendMail() {
        try {
            logger.error("测试");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Printing ERROR Statements", e);
        }
    }
}
