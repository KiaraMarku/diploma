package com.example.diploma.controller;

import com.example.diploma.entity.Class;
import com.example.diploma.service.ClassService;
import com.example.diploma.service.LabScheduleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller

public class ClassController {
    private ClassService classService;

    public ClassController(ClassService classService, LabScheduleService labScheduleService) {
        this.classService = classService;
    }

    @GetMapping("/class/{id}")
    public String viewClassDetails(@PathVariable("id") int classId, Model model) {
        Class classDetails = classService.findById(classId);
        model.addAttribute("classDetails", classDetails);
        return "class-details";
    }
}
