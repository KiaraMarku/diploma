package com.example.diploma.controller;

import com.example.diploma.dto.PasswordChangeDTO;
import com.example.diploma.entity.security.User;
import com.example.diploma.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {


    private UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    public String showHome() {
        return "common/home";
    }

    @RequestMapping("/showLoginPage")
    public String showLoginForm() {
        return "common/login-form";
    }

    @RequestMapping("/accessDenied")
    public String showAccessDenied() {
        return "access-denied";
    }

    @GetMapping("user/changePassword")
    public String changePassword() {
        return "common/change-password";
    }

    @PostMapping("user/changePassword")
    public String changePassword(@ModelAttribute PasswordChangeDTO passwordChangeDTO, Model model) {
        User user = userService.getLoggedInUser();

        if (!userService.checkPassword(user, passwordChangeDTO.getCurrentPassword())) {
            model.addAttribute("error", "Current password is incorrect");
            return "common/change-password";
        }

        if (!passwordChangeDTO.isValid()) {
            model.addAttribute("error", "New password and confirmation do not match");
            return "common/change-password";
        }

        userService.updatePassword(user, passwordChangeDTO.getNewPassword());
        model.addAttribute("success", "Password changed");
        return "common/change-password";
    }

}
