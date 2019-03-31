package me.shouheng.service.common.dao;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shouh, 2019/3/31-10:14
 */
public class SqlSessionHolder {

    private static final Logger logger = LoggerFactory.getLogger(SqlSessionHolder.class);

    private static ThreadLocal<SqlSession> sessionHolder = new ThreadLocal<>();

    private static SqlSessionFactory sqlMapper = SqlMapClientHolder.getInstance().getSqlMapper();

    public static void initSqlSession(ExecutorType type) {
        if (type == ExecutorType.BATCH) {
            initBatchSqlSession();
        } else {
            initReuseSqlSession();
        }
    }

    public static void initBatchSqlSession() {
        if (sessionHolder.get() == null) {
            logger.debug("initial batch session");
            sessionHolder.set(sqlMapper.openSession(ExecutorType.BATCH, false));
        }
    }

    public static void initReuseSqlSession() {
        if (sessionHolder.get() == null) {
            logger.debug("initial reuse session");
            sessionHolder.set(sqlMapper.openSession(ExecutorType.REUSE, false));
        }
    }

    public static void commitSession() {
        if (sessionHolder.get() == null) {
            return;
        }
        if (sessionHolder.get() != null) {
            sessionHolder.get().commit(true);
            sessionHolder.get().close();
            sessionHolder.remove();
        }
        logger.debug("session closed");
    }

    public static void rollbackSession() {
        if (sessionHolder.get() == null) {
            return;
        }
        if (sessionHolder.get() != null) {
            sessionHolder.get().rollback(true);
            sessionHolder.get().close();
            sessionHolder.remove();
        }
        logger.debug("session closed");
    }

    public static SqlSession getSession() {
        return sessionHolder.get();
    }

    public static void clearSession() {
        if (sessionHolder.get() == null) {
            return;
        }
        if (sessionHolder != null) {
            sessionHolder.get().close();
            sessionHolder.set(null);
        }
        logger.debug("SQL session closed.");
    }

}
