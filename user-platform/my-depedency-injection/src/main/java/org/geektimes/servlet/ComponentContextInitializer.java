package org.geektimes.servlet;

import org.geektimes.web.WebAppInitializer;
import org.geektimes.context.ClassicComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ComponentContextInitializer implements WebAppInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        ClassicComponentContext context = new ClassicComponentContext();
        context.init(servletContext);
    }
}
