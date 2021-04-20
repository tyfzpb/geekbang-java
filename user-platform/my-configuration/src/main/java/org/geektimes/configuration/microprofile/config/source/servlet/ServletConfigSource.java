package org.geektimes.configuration.microprofile.config.source.servlet;

import org.geektimes.configuration.microprofile.config.source.MapBasedConfigSource;

import javax.servlet.ServletConfig;
import java.util.Enumeration;
import java.util.Map;

import static java.lang.String.format;

public class ServletConfigSource extends MapBasedConfigSource {

    private Map<String,String> parameters;

    public ServletConfigSource(ServletConfig servletConfig) {
        super(format("Servlet[name:%s] Init Parameters", servletConfig.getServletName()), 600);
        getParameters(servletConfig);
    }


    private void getParameters(ServletConfig servletConfig){
        Enumeration<String> parameterNames = servletConfig.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            parameters.put(parameterName, servletConfig.getInitParameter(parameterName));
        }
    }

    @Override
    protected void prepareConfigData(Map configData) throws Throwable {
        this.parameters = configData;
    }
}
