package com.example.diploma.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping ("/")
    public String showHome(){
        return "common/home";
    }

    @RequestMapping("/showLoginPage")
    public String showLoginForm(){
        return "common/login-form";
    }

    @RequestMapping ("/accessDenied")
    public String showAccessDenied(){
        return "access-denied";
    }
}
