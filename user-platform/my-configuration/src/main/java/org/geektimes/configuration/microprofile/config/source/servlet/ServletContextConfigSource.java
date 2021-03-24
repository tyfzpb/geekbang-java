package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;

public class ServletContextConfigSource extends MapBasedConfigSource {

    private Map<String, String> source;

    public ServletContextConfigSource(ServletContext servletContext) {
        super("ServletContext Init Parameters", 500);

        Enumeration<String> parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            source.put(parameterName, servletContext.getInitParameter(parameterName));
        }
    }


    @Override
    public void prepareConfigData(Map configData) throws Throwable {
        this.source = configData;
    }


}
