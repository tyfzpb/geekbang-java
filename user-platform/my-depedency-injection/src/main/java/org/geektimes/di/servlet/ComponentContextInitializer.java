package org.geektimes.di.servlet;

import org.geektimes.di.context.ComponentContext;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class ComponentContextInitializer implements ServletContainerInitializer {

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext servletContext) throws ServletException {
        ComponentContext context = new ComponentContext();
        context.init(servletContext);
    }
}
