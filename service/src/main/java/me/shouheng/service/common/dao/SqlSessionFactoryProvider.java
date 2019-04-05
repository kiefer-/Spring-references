package me.shouheng.service.common.dao;

import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * @author shouh, 2019/4/5-16:21
 */
public class SqlSessionFactoryProvider {

    private SqlSessionFactoryBean sessionFactoryBean;

    private static volatile SqlSessionFactoryProvider instance;

    public static SqlSessionFactoryProvider getInstance() {
        if (instance == null) {
            synchronized (SqlSessionFactoryProvider.class) {
                if (instance == null) {
                    instance = new SqlSessionFactoryProvider();
                }
            }
        }
        return instance;
    }

    public void setSessionFactoryBean(SqlSessionFactoryBean sessionFactoryBean) {
        this.sessionFactoryBean = sessionFactoryBean;
    }

    public SqlSessionFactoryBean getSessionFactoryBean() {
        return sessionFactoryBean;
    }
}
