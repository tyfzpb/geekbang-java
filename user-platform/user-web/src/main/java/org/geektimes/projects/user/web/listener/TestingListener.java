package org.geektimes.projects.user.web.listener;

import org.geektimes.web.mvc.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
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
        initUsersTable();
    }

    private void initUsersTable() {
        ComponentContext context = ComponentContext.getInstance();
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
