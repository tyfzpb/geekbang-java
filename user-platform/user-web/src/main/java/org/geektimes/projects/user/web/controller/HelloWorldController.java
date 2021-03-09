package org.geektimes.projects.user.web.controller;

import org.geektimes.web.mvc.controller.PageController;

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
