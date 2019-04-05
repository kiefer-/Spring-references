package me.shouheng.service.base;

import me.shouheng.service.common.dao.SqlMapClientHolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author shouh, 2019/3/31-17:52
 */
@ActiveProfiles(value = "dev")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath*:spring/spring-dao.xml",
        "classpath*:spring/spring-service.xml",
        "classpath*:spring/spring-shiro.xml"})
public class SpringBaseTest {

    public SpringBaseTest() {
        SqlMapClientHolder.setEnvironmentId(SqlMapClientHolder.ENV_DRUID_ID);
        SqlMapClientHolder.setTestFlag(true);

        System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.parsers.SAXParserFactory", "com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl");
    }

//    @Before
//    public void testBefore() {
//        SqlSessionHolder.initReuseSqlSession();
//    }
//
//    @After
//    public void testAfter() {
//        SqlSessionHolder.rollbackSession();
//    }

}
