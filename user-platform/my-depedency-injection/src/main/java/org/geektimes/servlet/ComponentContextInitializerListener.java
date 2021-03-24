package org.geektimes.servlet;

import org.geektimes.context.ComponentContext;
import org.geektimes.context.ClassicComponentContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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
        ComponentContext context = ClassicComponentContext.getInstance();
        context.destroy();
    }

}
