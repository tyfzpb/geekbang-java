package org.geektimes.spring.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
public class HelloController {

    @GetMapping("/1")
    public String hello1(){
        return "hello1, spring security";
    }

    @GetMapping("/2")
    public String hello2(){
        return "hello2, spring security";
    }

    @GetMapping("/3")
    public String hello3(){
        return "hello3, spring security";
    }
}
