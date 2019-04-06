package me.shouheng.common;

import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author shouh, 2019/4/6-15:44
 */
public class QuartzTest {

    private static final Logger logger = LoggerFactory.getLogger(QuartzTest.class);

    /**
     * 触发器的种类：
     * 1. {@link CalendarIntervalTriggerImpl}
     * 2. {@link CalendarIntervalTriggerImpl}
     * 3. {@link CronTriggerImpl} 支持类似日历的重复间隔，而不是单一的时间间隔
     * 4. {@link SimpleTriggerImpl}
     *
     * {@link org.quartz.simpl.RAMJobStore} 等其他种类的存储器
     *
     * 参考：
     * 1. <a href="http://www.opensymphony.com/quartz/wikidocs/Tutorial.html">“The Official Quartz Tutorial”</a>：
     * 直接从 OpenSymphony 学习关于 Quartz 调度框架的更多内容。
     * 2. <a href="http://www.opensymphony.com/quartz/wikidocs/Configuration.html">Quartz configuration<a/>：
     * 关于 Quartz 配置的一切，包括 Quartz 可配置属性的列表。
     * 3. <a href="http://safari.phptr.com/0131886703">Quartz Job Scheduling Framework（Chuck Cavaness; Prentice Hall，2006 年 6 月）<a/>：
     * 学习 Quartz 的推荐读物。
     *
     * @throws SchedulerException 异常
     * @throws InterruptedException 异常
     */
    @Test
    public void testScheduler() throws SchedulerException, InterruptedException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        Trigger trigger = new SimpleTriggerImpl();
        ((SimpleTriggerImpl) trigger).setName("Trigger");
        ((SimpleTriggerImpl) trigger).setStartTime(new Date());
        // 如果想要启动重复的任务，那么重复的次数和间隔的时间都是必不可少的
        ((SimpleTriggerImpl) trigger).setRepeatCount(-1);
        ((SimpleTriggerImpl) trigger).setRepeatInterval(1000);

        JobDetail job = new JobDetailImpl();
        ((JobDetailImpl) job).setName("job");
        ((JobDetailImpl) job).setJobClass(HelloQuartz.class);

        scheduler.scheduleJob(job, trigger);
        scheduler.start();

        Thread.sleep(10000);
        scheduler.shutdown(true);
    }

    public static class HelloQuartz implements Job {

        @Override
        public void execute(JobExecutionContext context) {
            JobDetail detail = context.getJobDetail();
            String name = detail.getJobDataMap().getString("name");
            logger.debug("Say hello to {} at {}", name, new Date());
            logger.debug("Next fire time is {}", context.getNextFireTime());
        }
    }

}
