package org.geektimes.projects.user.web.controller;

import org.eclipse.microprofile.config.Config;
import org.geektimes.oauth.config.AuthConfig;
import org.geektimes.oauth.request.GiteeAuthRequest;
import org.geektimes.projects.user.domain.User;
import org.geektimes.web.mvc.controller.PageController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;


public class HelloWorldController implements PageController {

    @GET
    @POST
    @Path("/registry") // /hello/world -> HelloWorldController
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        // test config
        Config config = (Config)request.getServletContext().getAttribute(Config.class.getName());
        if(config != null){
            String testAppName = config.getValue("testAppName",String.class);
            System.out.println(testAppName);
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

    @POST
    @Path("/testRestPost")
    public User testRestPost(User user){
        System.out.println(user);
        user.setId(111L);
        return user;
    }

    @Path("/login/oauth2/code/github")
    public String loginCallbackGithub(String code,HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("source","github");
        return "oauthCallback.jsp";
    }

    @Path("/login/oauth2/code/gitee")
    public String loginCallbackGitee(String code,HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("source","gitee");
        return "oauthCallback.jsp";
    }




}
