package org.geektimes.projects.user.web.controller;

import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


@Path("/user")
public class UserController implements RestController {


    @GET
    @POST
    @Path("/register")
    public String register(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        System.out.println("register......");
        return "注册页面";
    }
}
