package me.shouheng.service.common;

import me.shouheng.common.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author shouh, 2019/4/6-12:18
 */
@Component("applicationContextHolder")
public class ApplicationContextHolder implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextHolder.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("On set application context");
        DataInitializer.init();
    }
}
