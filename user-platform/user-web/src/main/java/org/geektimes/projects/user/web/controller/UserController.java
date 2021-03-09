package org.geektimes.projects.user.web.controller;

import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.service.impl.UserServiceImpl;
import org.geektimes.projects.user.validator.bean.validation.ValidationResult;
import org.geektimes.projects.user.validator.bean.validation.ValidationUtils;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.management.remote.rmi._RMIConnection_Stub;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.HashMap;
import java.util.Map;


public class UserController implements RestController {

    private UserService userService = ComponentContext.getInstance().getComponent("bean/UserService");

    @GET
    @POST
    @Path("/doRegistry")
    public Object doRegistry(String name,
                             String password,
                             String email,
                             String phoneNumber) throws Throwable {
        Map<String, Object> result = new HashMap<>(2);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        Map<String,String> validationResult = validateSaveUser(user);
        if(validationResult != null){
            result.put("code", 0);
            result.put("message", validationResult);
        }else{
            boolean flag = userService.register(user);
            if(flag){
                result.put("code", 1);
                result.put("message", "注册成功");
            }else{
                result.put("code", 0);
                result.put("message", "注册失败");
            }
        }
        return result;
    }

    @POST
    @GET
    @Path("/validateEmail")
    public Object validateEmail(String email){
        Map<String,Object> result = new HashMap<>(2);
        result.put("code",1);
        User user = new User();
        user.setEmail(email);
        ValidationResult validationResult = ValidationUtils.validateProperty(user,"email",Default.class);
        if(validationResult.isHasErrors()){
            result.put("code",0);
            result.put("message",validationResult.getErrorMsg().get("email"));
        }
        return result;
    }

    @POST
    @GET
    @Path("/validatePassword")
    public Object validatePassword(String password){
        Map<String,Object> result = new HashMap<>(2);
        result.put("code",1);
        User user = new User();
        user.setPassword(password);
        ValidationResult validationResult = ValidationUtils.validateProperty(user,"password",Default.class);
        if(validationResult.isHasErrors()){
            result.put("code",0);
            result.put("message",validationResult.getErrorMsg().get("password"));
        }
        return result;
    }

    @POST
    @GET
    @Path("/validatePhoneNumber")
    public Object validatePhoneNumber(String phoneNumber){
        Map<String,Object> result = new HashMap<>(2);
        result.put("code",1);
        User user = new User();
        user.setPhoneNumber(phoneNumber);
        ValidationResult validationResult = ValidationUtils.validateProperty(user,"phoneNumber",Default.class);
        if(validationResult.isHasErrors()){
            result.put("code",0);
            result.put("message",validationResult.getErrorMsg().get("phoneNumber"));
        }
        return result;
    }

    private Map<String,String> validateSaveUser(User user){
        ValidationResult result = ValidationUtils.validateEntity(user,Default.class);
        if(result.isHasErrors()){
            return result.getErrorMsg();
        }
        return null;
    }

    @GET
    @POST
    @Path("/checkUserName")
    public Object checkUserName(String name) throws Throwable{
        Map<String,Object> result = new HashMap<>(2);
        result.put("code",0);
        if(name == null && name.trim().isEmpty()){
            result.put("message","用户名不能为空");
            return result;
        }
        boolean flag = userService.checkUserName(name);
        if(flag){
            result.put("code","1");
        }else{
            result.put("message","该用户名已被占用，请更换");
        }
        return result;
    }
}
