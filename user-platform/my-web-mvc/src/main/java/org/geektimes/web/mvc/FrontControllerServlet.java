package org.geektimes.web.mvc;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.geektimes.context.ComponentContext;
import org.geektimes.context.ClassicComponentContext;
import org.geektimes.web.mvc.controller.Controller;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.substringAfter;

public class FrontControllerServlet extends HttpServlet {

    /**
     * 请求路径和 Controller 的映射关系缓存
     */
    private final Map<String, Controller> controllersMapping = new HashMap<>();

    /**
     * 请求路径和 {@link HandlerMethodInfo} 映射关系缓存
     */
    private final Map<String, HandlerMethodInfo> handleMethodInfoMapping = new HashMap<>();

    private ComponentContext context = null;

    /**
     * 初始化 Servlet
     *
     * @param servletConfig
     */
    public void init(ServletConfig servletConfig) {
        ComponentContext context = (ComponentContext) servletConfig.getServletContext().getAttribute(ClassicComponentContext.CONTEXT_NAME);
        this.context = context;
        initHandleMethods();
    }

    /**
     * 读取所有的 RestController 的注解元信息 @Path
     * 利用 ServiceLoader 技术（Java SPI）
     */
    private void initHandleMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = Objects.isNull(pathFromClass) ? "" : pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            // 处理方法支持的 HTTP 方法集合
            for (Method method : publicMethods) {
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod == null) {
                    continue;
                }
                String path = requestPath + pathFromMethod.value();
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                handleMethodInfoMapping.put(path,
                        new HandlerMethodInfo(path, method, supportedHttpMethods));
                controllersMapping.put(path, controller);
            }
            injectControllerField(controller);
        }
    }

    /**
     * 注入Cotroller 字段
     *
     * @param controller
     */
    private void injectControllerField(Controller controller) {
        Arrays.stream(controller.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Resource.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(controller, this.context.getComponent(field.getAnnotation(Resource.class).name()));
                    } catch (IllegalAccessException e) {
                        new RuntimeException(e);
                    }
                });
    }


    /**
     * 获取处理方法中标注的 HTTP方法集合
     *
     * @param method 处理方法
     * @return
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }

        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }

        return supportedHttpMethods;
    }

    /**
     * SCWCD
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 建立映射关系
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // contextPath  = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        String prefixPath = servletContextPath;
        // 映射路径（子路径）
        String requestMappingPath = substringAfter(requestURI,
                StringUtils.replace(prefixPath, "//", "/"));
        // 映射到 Controller
        Controller controller = controllersMapping.get(requestMappingPath);

        if (controller != null) {

            HandlerMethodInfo handlerMethodInfo = handleMethodInfoMapping.get(requestMappingPath);

            try {
                if (handlerMethodInfo != null) {

                    String httpMethod = request.getMethod();

                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        // HTTP 方法不支持
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }

                    if (controller instanceof PageController) {
                        Method handlerMethod = handlerMethodInfo.getHandlerMethod();

                        Map<String, String[]> parameterMap = request.getParameterMap();

                        Parameter[] methodParameters = handlerMethod.getParameters();

                        Object[] paramValues = new Object[handlerMethod.getParameterCount()];
                        for (int i = 0; i < methodParameters.length; i++) {
                            if (HttpServletRequest.class == methodParameters[i].getType()) {
                                paramValues[i] = request;
                                continue;
                            }
                            if (HttpServletResponse.class == methodParameters[i].getType()) {
                                paramValues[i] = response;
                                continue;
                            }
                            if (Integer.class == methodParameters[i].getType()) {
                                paramValues[i] = Integer.valueOf(Arrays.toString(parameterMap.get(methodParameters[i].getName()))
                                        .replaceAll("\\[|\\]", "").replaceAll("\\s", ""));
                                continue;
                            }
                            // String Long Boolean ...

                            Class<?> type = methodParameters[i].getType();
                            Object instance = type.newInstance();

                            BeanInfo beanInfo = Introspector.getBeanInfo(type);
                            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                                String name = propertyDescriptor.getName();
                                if (parameterMap.containsKey(name)) {
                                    String value = Arrays.toString(parameterMap.get(name))
                                            .replaceAll("\\[|\\]", "").replaceAll("\\s", "");

                                    Method writeMethod = propertyDescriptor.getWriteMethod();

                                    if (Integer.class == propertyDescriptor.getPropertyType()) {
                                        writeMethod.invoke(instance, Integer.valueOf(value));
                                    }

                                    if (String.class == propertyDescriptor.getPropertyType()) {
                                        writeMethod.invoke(instance, value);
                                    }
                                }
                            }

                            paramValues[i] = instance;
                        }

                        String viewPath = (String) handlerMethod.invoke(controller, paramValues);
                        // 页面请求 forward
                        // request -> RequestDispatcher forward
                        // RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
                        // ServletContext -> RequestDispatcher forward
                        // ServletContext -> RequestDispatcher 必须以 "/" 开头
                        ServletContext servletContext = request.getServletContext();

                        if (!viewPath.startsWith("/")) {
                            viewPath = "/" + viewPath;
                        }
                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                        requestDispatcher.forward(request, response);
                        return;
                    } else if (controller instanceof RestController) {
                        Map<String, String[]> parameterMap = request.getParameterMap();

                        Method handlerMethod = handlerMethodInfo.getHandlerMethod();

                        Parameter[] parameters = handlerMethod.getParameters();

                        Object[] paramValues = new Object[handlerMethod.getParameterCount()];
                        for (int i = 0; i < parameters.length; i++) {
                            String value = Arrays.toString(parameterMap.get(parameters[i].getName()))
                                    .replaceAll("\\[|\\]", "").replaceAll("\\s", "");

                            paramValues[i] = value;
                        }

                        Object result = handlerMethod.invoke(controller, paramValues);

                        Object jsonResult = JSONObject.toJSON(result);
                        response.setContentType("text/plain");
                        response.getWriter().write(jsonResult.toString());
                    }

                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable.getCause());
                }
            }
        }
    }

//    private void beforeInvoke(Method handleMethod, HttpServletRequest request, HttpServletResponse response) {
//
//        CacheControl cacheControl = handleMethod.getAnnotation(CacheControl.class);
//
//        Map<String, List<String>> headers = new LinkedHashMap<>();
//
//        if (cacheControl != null) {
//            CacheControlHeaderWriter writer = new CacheControlHeaderWriter();
//            writer.write(headers, cacheControl.value());
//        }
//    }
}
