package me.shouheng.common.mail;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author shouh, 2019/4/5-22:51
 */
@Data
public class MailSender {

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    private String auth;

    private String host;

    private String userName;

    private String password;

    /**
     * 发送邮件到指定的邮箱
     *
     * @param fromAddress 发件邮箱
     * @param toAddress 接收邮箱
     * @param subject 邮件的主题
     * @param text 邮件内容
     * @throws Exception 异常
     */
    public void sendMail(String fromAddress, String[] toAddress, String subject, String text) throws Exception {
        Authenticator au = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Message message = new MimeMessage(Session.getInstance(getProperties(), au));
        message.setFrom(new InternetAddress(fromAddress));
        message.setSubject(subject);
        message.setText(text);
        message.setRecipients(Message.RecipientType.TO, Arrays.stream(toAddress)
                .map(address -> {
                    try {
                        return new InternetAddress(address);
                    } catch (AddressException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).toArray(InternetAddress[]::new));
        Transport.send(message);
        logger.info("Send email from {} to {}", fromAddress, toAddress);
    }

    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.host", host);
        return props;
    }

    public static void main(String...args) {
        MailSender mailSender = new MailSender();
        mailSender.setUserName("spring_references@163.com");
        mailSender.setPassword("myspring0");
        mailSender.setHost("smtp.163.com");
        mailSender.setAuth("true");
        try {
            mailSender.sendMail("spring_references@163.com", new String[]{"shouheng2015@gmail.com"}, "Hello!", "Hello world from JMail.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
