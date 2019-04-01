package me.shouheng.service.common.util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author shouh, 2019/4/1-21:26
 */
public class ErrorDispUtils {

    private static Logger logger = LoggerFactory.getLogger(ErrorDispUtils.class);

    private static final String CONFIG_FILE = "error-disp.properties";

    private static ErrorDispUtils instance = new ErrorDispUtils();

    private static Configuration config;

    public static ErrorDispUtils getInstance() {
        return instance;
    }

    private ErrorDispUtils() {
        try {
            config = new PropertiesConfiguration(CONFIG_FILE);
        } catch (Exception e) {
            logger.error("ErrorDispUtils initialize error" ,e);
        }
    }

    public String getValue(String key) {
        return config.getString(key);
    }

    public static void main(String[] args) {
        String message = ErrorDispUtils.getInstance().getValue("E000000000000000");
        message = MessageFormat.format(message, "aaa", "bb cc");
        System.out.println(message);
    }
}
