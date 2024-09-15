package com.example.diploma.controller;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.Student;
import com.example.diploma.entity.StudentClassGrade;
import com.example.diploma.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private  Student student;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        this.student=studentService.getLoggedInStudent();
        model.addAttribute("student", student);
        return "student/student-profile";
    }


    @GetMapping("/classes")
    public String viewClasses(Model model) {
        Student student = studentService.getLoggedInStudent();
        Set<Class> classes = studentService.getClassesForStudent(student);


        Map<Class, Optional<StudentClassGrade>> finalGrades = new HashMap<>();
        for (Class theClass : classes) {
            finalGrades.put(theClass, studentService.getFinalGradeForClass(student.getId(), theClass.getId()));
        }


        List<StudentClassGrade> passedClasses = studentService.getPassedClassesForStudent(student.getId());
        double averageGrade = studentService.calculateAverageGrade(passedClasses);
        String avg= String.format("%.2f", averageGrade);
        int totalCredits = passedClasses.stream().mapToInt(cg -> cg.getTheClass().getCredits()).sum();

        model.addAttribute("classes", classes);
        model.addAttribute("finalGrades", finalGrades);
        model.addAttribute("averageGrade", avg);
        model.addAttribute("totalCredits", totalCredits);

        return "student/student-classes";
    }









}

