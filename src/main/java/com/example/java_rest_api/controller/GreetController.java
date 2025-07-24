package com.example.java_rest_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetController {

    @GetMapping("/welcome")
    public String getMessage(){
        return "<h1>  Hello from Spring Rest Controller: Auto deployed~ </h1>";
    }

}
