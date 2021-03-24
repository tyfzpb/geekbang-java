package org.geektimes.projects.user.web.listener;

import org.geektimes.context.ClassicComponentContext;
import org.geektimes.projects.user.management.UserManager;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 测试用途
 */
@Deprecated
public class TestingListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        registerMBan();
    }

    private void initUsersTable() {
        ClassicComponentContext context = ClassicComponentContext.getInstance();
        DBConnectionManager dbConnectionManager = context.getComponent("bean/DBConnectionManager");
        Connection connection = dbConnectionManager.getConnection();
        Statement statement = null;
        try{
            statement = connection.createStatement();
            //statement.execute(DBConnectionManager.DROP_USERS_TABLE_DDL_SQL);
            statement.execute(DBConnectionManager.CREATE_USERS_TABLE_DDL_SQL);
        }catch (SQLException e){
            logger.log(Level.SEVERE,e.getMessage());
        }
    }


    private void registerMBan(){
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            // 为 UserMXBean 定义 ObjectName
            ObjectName objectName = new ObjectName("org.geektimes.projects.user.management:type=User");
            // 创建 UserMBean 实例
            User user = new User();
            user.setId(111L);
            user.setName("ty");
            user.setPassword("*******");
            mBeanServer.registerMBean(new UserManager(user), objectName);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void testUser(EntityManager entityManager) {
        User user = new User();
        user.setName("小马哥");
        user.setPassword("******");
        user.setEmail("mercyblitz@gmail.com");
        user.setPhoneNumber("abcdefg");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();
        System.out.println(entityManager.find(User.class, user.getId()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
