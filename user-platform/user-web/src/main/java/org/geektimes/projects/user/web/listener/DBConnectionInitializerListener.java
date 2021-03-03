package org.geektimes.projects.user.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import javax.annotation.Resource;
import org.geektimes.projects.user.sql.DBConnectionManager;

@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {

    /*
     * 使用Resource注解成员变量，通过名字查找web.xml中配置的数据源并注入进来
     * lookup：指定目录处的名称，此属性是固定的
     * name：指定数据源的名称，即数据源处配置的name属性
     */
    @Resource(lookup="java:/comp/env", name="jdbc/UserPlatformDB")
    private DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DBConnectionManager.setDataSource(dataSource);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
