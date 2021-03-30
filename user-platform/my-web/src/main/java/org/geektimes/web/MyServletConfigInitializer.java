package org.geektimes.web;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@HandlesTypes(WebApplicationInitializer.class)
public class MyServletConfigInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext) throws ServletException {

        List<WebApplicationInitializer> waiList = new ArrayList<>();

        if(webAppInitializerClasses != null){
            for (Class<?> waiClass : webAppInitializerClasses) {

                if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
                        WebApplicationInitializer.class.isAssignableFrom(waiClass)){

                    try{
                        waiList.add((WebApplicationInitializer)waiClass.newInstance());
                    }catch (Throwable ex) {
                        throw new ServletException("Failed to instantiate WebAppInitializer class", ex);
                    }

                }

            }

        }

        if(waiList.isEmpty()){
            servletContext.log("No WebAppInitializer types detected on classpath");
            return;
        }

        waiList.sort(WebApplicationInitializerOrdinalComparator.INSTANCE);

        for(WebApplicationInitializer wai : waiList){
            wai.onStartup(servletContext);
        }
    }
}
