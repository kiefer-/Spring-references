package me.shouheng.service.basic;

import com.alibaba.druid.pool.DruidDataSource;
import com.mysql.jdbc.Driver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author shouh, 2019/3/30-16:41
 */
public class DruidConnectionTest {

    private Logger logger = LoggerFactory.getLogger(DruidConnectionTest.class);

    private static final String DB_URL = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "qweasdzxc";

    private DruidDataSource dataSource;

    @Before
    public void before() {
        logger.debug("----before");
    }

    @After
    public void after() {
        logger.debug("----after");
    }

    @Before
    public void prepare() {
        dataSource = new DruidDataSource();
        try {
            dataSource.setDriver(new Driver());
            dataSource.setUrl(DB_URL);
            dataSource.setUsername(DB_USERNAME);
            dataSource.setPassword(DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void druidConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM test_table");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        stmt.close();
        connection.close();
    }
}
