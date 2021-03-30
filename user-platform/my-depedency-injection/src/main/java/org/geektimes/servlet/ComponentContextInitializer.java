package org.geektimes.servlet;

import org.geektimes.web.WebApplicationInitializer;
import org.geektimes.context.ClassicComponentContext;

import javax.annotation.Priority;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Priority(value=Integer.MIN_VALUE + 1)
public class ComponentContextInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ClassicComponentContext context = new ClassicComponentContext();
        context.init(servletContext);
    }
}
