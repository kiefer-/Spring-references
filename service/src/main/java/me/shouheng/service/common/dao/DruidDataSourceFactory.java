package me.shouheng.service.common.dao;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import java.util.Properties;

/**
 * @author shouh, 2019/3/31-22:14
 */
public class DruidDataSourceFactory extends UnpooledDataSourceFactory {

    public DruidDataSourceFactory() {
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        dataSource = DruidDataSourceProvider.getInstance().getDataSource();
    }

}
