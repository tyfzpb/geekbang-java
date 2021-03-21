package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.geektimes.web.mvc.controller.PageController;
import org.geetimes.util.ThreadLocalUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


public class HelloWorldController implements PageController {

    @GET
    @POST
    @Path("/registry") // /hello/world -> HelloWorldController
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        ClassLoader classLoader = request.getServletContext().getClassLoader();
        ClassLoader classLoader1 = this.getClass().getClassLoader();
        ClassLoader classLoader2 = Thread.currentThread().getContextClassLoader();
        Config config = ThreadLocalUtil.get(Config.class.getName());
        config = ConfigProvider.getConfig(this.getClass().getClassLoader());
        config = (Config)request.getServletContext().getAttribute(Config.class.getName());
        if(config != null){
            String testAppName = config.getValue("testAppName",String.class);
        }
        return "registry.jsp";
    }

    @Path("/registry-success")
    public String registrySuccess() {
        return "registry-success.jsp";
    }

    @Path("/login")
    public String login(){
        return "login-form.jsp";
    }

    @Path("/login-success")
    public String loginSuccess(){
        return "login-success.jsp";
    }
}
