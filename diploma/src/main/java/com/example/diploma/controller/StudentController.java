package com.example.diploma.controller;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.Student;
import com.example.diploma.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Student student = studentService.getLoggedInStudent(); // Assuming you have this method
        model.addAttribute("student", student);
        return "student/student-profile";
    }

    @GetMapping("/classes")
    public String viewClasses(Model model) {
        Student student = studentService.getLoggedInStudent();
        Set<Class> classes = studentService.getClassesForStudent(student);
        model.addAttribute("classes", classes);
        return "student/student-classes";
    }
}

