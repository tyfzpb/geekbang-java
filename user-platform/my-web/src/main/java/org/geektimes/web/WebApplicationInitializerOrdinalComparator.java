package org.geektimes.web;

import javax.annotation.Priority;
import java.util.Comparator;

public class WebApplicationInitializerOrdinalComparator implements Comparator<WebApplicationInitializer> {

    public static final Comparator<WebApplicationInitializer> INSTANCE = new WebApplicationInitializerOrdinalComparator();

    private WebApplicationInitializerOrdinalComparator() {
    }

    @Override
    public int compare(WebApplicationInitializer o1, WebApplicationInitializer o2) {
        Priority priority1 = o1.getClass().getAnnotation(Priority.class);
        Priority priority2 = o2.getClass().getAnnotation(Priority.class);
        int value1 = null == priority1 ? Integer.MAX_VALUE : priority1.value();
        int value2 = null == priority2 ? Integer.MAX_VALUE : priority2.value();
        return Integer.compare(value1,value2);
    }
}
