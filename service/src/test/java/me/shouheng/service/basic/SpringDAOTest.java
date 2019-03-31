package me.shouheng.service.basic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 测试 Druid 数据库连接池是否连接正确，需要使用 {@link ActiveProfiles} 注解指定要启动的环境
 * 然后使用 {@link ContextConfiguration} 指定要加载的配置文件，最后注意 {@link RunWith} 的
 * 使用的条件是 JUnit 包在 4.12 及以上。
 *
 * @author shouh, 2019/3/30-18:53
 */
@ActiveProfiles(value = "dev")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:spring/spring-dao.xml"})
public class SpringDAOTest {

    private static final Logger logger = LoggerFactory.getLogger(SpringDAOTest.class);

    @Autowired
    private DataSource dataSource;

    /**
     * 测试 Druid 数据连接是否正常可用
     *
     * @throws SQLException 异常
     */
    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM test_table");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            logger.debug(rs.getString(1));
        }
        stmt.close();
        connection.close();
    }
}
