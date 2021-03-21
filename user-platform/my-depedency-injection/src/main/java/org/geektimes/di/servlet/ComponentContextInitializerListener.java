package org.geektimes.di.servlet;

import org.geektimes.di.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * {@link ComponentContext}
 * ContextLoaderListener
 */
public class ComponentContextInitializerListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ComponentContext context = ComponentContext.getInstance();
        context.destroy();
    }

}
