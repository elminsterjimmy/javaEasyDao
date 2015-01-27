package com.elminster.easydao.db.ds;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.elminster.easydao.db.constants.DataSourceConstants;
import com.elminster.easydao.db.context.DataSourceInfo;
import com.elminster.easydao.db.context.IContext;

public class DataSourceFactory implements DataSourceConstants {

  public static final DataSourceFactory INSTANCE = new DataSourceFactory();
  
  public DataSource getDataSource(IContext context, String dsName) {
    BasicDataSource ds = new BasicDataSource();
    DataSourceInfo dsi = context.getDataSourceInforamtion(dsName);
    String driverClassName = dsi.getDriverClassName();
    String url = dsi.getUrl();
    String userName = dsi.getUsername();
    String password = dsi.getPassword();
    ds.setDriverClassName(driverClassName);
    ds.setUrl(url);
    ds.setUsername(userName);
    ds.setPassword(password);
    return ds;
  }
  
  public DataSource getDataSource(Properties properties) {
    BasicDataSource ds = new BasicDataSource();
    String driverClassName = properties.getProperty(DATASOURCE_DRIVER_CLASS_NAME);
    String url = properties.getProperty(DATASOURCE_URL);
    String userName = properties.getProperty(DATASOURCE_USERNAME);
    String password = properties.getProperty(DATASOURCE_PASSWORD);
    ds.setDriverClassName(driverClassName);
    ds.setUrl(url);
    ds.setUsername(userName);
    ds.setPassword(password);
    return ds;
  }
}
