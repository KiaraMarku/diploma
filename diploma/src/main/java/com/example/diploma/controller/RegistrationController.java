package com.example.diploma.controller;

import com.example.diploma.exception.UsernameAlreadyExistsException;
import com.example.diploma.dto.ProfessorRegistrationDTO;
import com.example.diploma.dto.StudentRegistrationDTO;
import com.example.diploma.service.DepartmentService;
import com.example.diploma.service.GroupService;
import com.example.diploma.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@Controller
@RequestMapping ("/register")
public class RegistrationController {

    private RegistrationService registrationService;
    private GroupService groupService;
    private DepartmentService departmentService;

    public RegistrationController(RegistrationService registrationService, GroupService groupService, DepartmentService departmentService) {
        this.registrationService = registrationService;
        this.groupService = groupService;
        this.departmentService = departmentService;
    }




    @GetMapping("/studentForm")
       public String showStudentRegistrationForm(Model model) {
        model.addAttribute("student", new StudentRegistrationDTO());
        model.addAttribute("groups", groupService.findAll());
        return "student/student-registration";
    }

    @PostMapping("/student")
    public String registerStudent(@ModelAttribute("student") @Valid StudentRegistrationDTO registrationDto,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("groups", groupService.findAll());
            return "student/student-registration";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
            model.addAttribute("groups", groupService.findAll());
            return "student/student-registration";
        }

        try {
            registrationService.registerStudent(registrationDto);
        } catch (UsernameAlreadyExistsException | ParseException e) {
            result.rejectValue("username", null, "Username already exists");
            model.addAttribute("groups", groupService.findAll());
            return "student/student-registration";
        }

        return "home";
    }

    @GetMapping("/professorForm")

    public String showProfessorRegistrationForm(Model model) {
        model.addAttribute("professor", new ProfessorRegistrationDTO());
        model.addAttribute("departments", departmentService.findAll());
        return "professor/professor-registration";
    }

    @PostMapping("professor")
    public String registerProfessor(@ModelAttribute("professor") @Valid ProfessorRegistrationDTO registrationDto,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "professor/professor-registration";
        }

        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Passwords do not match");
            model.addAttribute("departments", departmentService.findAll());
            return "professor/professor-registration";
        }

        try {
            registrationService.registerProfessor(registrationDto);
        } catch (UsernameAlreadyExistsException e) {
            result.rejectValue("username", null, "Username already exists");
            model.addAttribute("departments", departmentService.findAll());
            return "professor/professor-registration";
        }

        return "redirect:/register/professor?success";
    }
}

