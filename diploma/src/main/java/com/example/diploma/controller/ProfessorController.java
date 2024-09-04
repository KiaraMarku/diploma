package com.example.diploma.controller;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.Professor;
import com.example.diploma.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/professor")
public class ProfessorController {

    private  ProfessorService professorService;

    private  final ClassService classService;

    private final LabScheduleService labScheduleService;


    public ProfessorController(ProfessorService professorService, GroupService groupService, ClassService classService, LabScheduleService labScheduleService, ScheduleService scheduleService) {
      this.professorService=professorService;
        this.classService = classService;
        this.labScheduleService = labScheduleService;
    }

    @GetMapping("/classes")
    public String viewClasses(Model model) {
        Professor professor = professorService.getLoggedInProfessor();
        List<Class> classes = professorService.getClassesForProfessor(professor);
        model.addAttribute("classes", classes);
        return "professor/professor-classes";
    }

}
