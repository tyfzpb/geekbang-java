package org.geektimes.projects.user.web.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import javax.annotation.Resource;
import org.geektimes.projects.user.sql.DBConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {

    private DBConnectionManager dbConnectionManager ;

//    @Resource(lookup="java:/comp/env", name="jdbc/UserPlatformDB")
//    private  DataSource dataSource;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
        Context cxt=new InitialContext();

        //获取与逻辑名相关联的数据源对象
        DataSource ds=(DataSource)cxt.lookup("java:comp/env/jdbc/UserPlatformDB");
        dbConnectionManager = new DBConnectionManager(ds);


          //  dbConnectionManager = new DBConnectionManager();
           // String databaseURL = "jdbc:derby:db/user-platform;create=true";
           // Connection connection = DriverManager.getConnection(databaseURL);

        Connection connection = ds.getConnection();

            Statement statement = connection.createStatement();
            statement.execute(DBConnectionManager.CREATE_USERS_TABLE_DDL_SQL);
            connection.close();
        }catch (Exception e){
            sce.getServletContext().log(e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(dbConnectionManager != null){
            dbConnectionManager.releaseConnection();
        }
    }
}
