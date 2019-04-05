package me.shouheng.service.common.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shouh, 2019/3/31-10:14
 */
public class SqlMapClientHolder {

    private static final Logger logger = LoggerFactory.getLogger(SqlSessionHolder.class);

    public static final String ENV_DEV_ID = "pooled";

    public static final String ENV_DRUID_ID = "druid";

    private static SqlSessionFactory sqlMapper;

    private static String environmentId = ENV_DRUID_ID;

    private static SqlMapClientHolder holder;

    private static boolean testFlag = false;

    public synchronized static SqlMapClientHolder getInstance() {
        if (sqlMapper == null) {
            try {
                sqlMapper = SqlSessionFactoryProvider.getInstance().getSessionFactoryBean().getObject();
                logger.info("SqlSessionFactory built successfully.");
                if (holder == null) {
                    holder = new SqlMapClientHolder();
                }
            } catch (Exception e) {
                logger.error("SqlMapClientHolder initialize error", e);
                throw new RuntimeException(e);
            }
        }
        return holder;
    }

    private SqlMapClientHolder() { }

    public SqlSessionFactory getSqlMapper() {
        return sqlMapper;
    }

    public static void setEnvironmentId(String environmentId) {
        SqlMapClientHolder.environmentId = environmentId;
    }

    public static void setTestFlag(boolean testFlag) {
        SqlMapClientHolder.testFlag = testFlag;
    }
}
