package test.org.geektimes.projects.user;

import org.geektimes.web.mvc.controller.Controller;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.ServiceLoader;

public class Test {

    public static void main(String[] args){
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            for(Method method : publicMethods){
                System.out.println(controllerClass + ":" + method.getName());
            }
        }
    }
}
