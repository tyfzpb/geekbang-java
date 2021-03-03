package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;


public class UserController implements RestController {

    private UserService userService = new UserServiceImpl();

    @GET
    @POST
    @Path("/doRegistry")
    public Object doRegistry(String name,
                             String password,
                             String email,
                             String phoneNumber) throws Throwable {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        userService.register(user);
        user = userService.queryUserByNameAndPassword(name,password);
        Map<String, Object> result = new HashMap<>(2);
        result.put("code", 1);
        result.put("message", "注册成功");
        result.put("user",user);
        return result;
    }
}
