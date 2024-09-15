package com.example.diploma.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("message", "Something went wrong. Please try again later.");
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "Page not found. Please check the URL.");
        return "error";
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResource(NoResourceFoundException ex, Model model) {
        model.addAttribute("message", "Page not found. Please check the URL.");
        return "error";
    }


}
