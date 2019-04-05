package me.shouheng.service.basic;

import me.shouheng.common.mail.MailSender;
import me.shouheng.service.base.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shouh, 2019/4/5-23:29
 */
public class MailSenderTest extends SpringBaseTest {

    @Autowired
    private MailSender mailSender;

    @Test
    public void sendEmail() {
        try {
            mailSender.sendMail("spring_references@163.com", new String[]{"shouheng2015@gmail.com"}, "Hello!", "Hello world from JMail.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
