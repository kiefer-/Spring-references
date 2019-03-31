package me.shouheng.service.common.dao;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author shouh, 2019/3/31-22:42
 */
public class DruidDataSourceProvider {

    private DruidDataSource dataSource;

    private static volatile DruidDataSourceProvider instance;

    public static DruidDataSourceProvider getInstance() {
        if (instance == null) {
            synchronized (DruidDataSourceProvider.class) {
                if (instance == null) {
                    instance = new DruidDataSourceProvider();
                }
            }
        }
        return instance;
    }

    public void setDataSource(DruidDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DruidDataSource getDataSource() {
        return dataSource;
    }
}
