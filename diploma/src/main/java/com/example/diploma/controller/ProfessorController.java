package com.example.diploma.controller;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.Professor;
import com.example.diploma.entity.Student;
import com.example.diploma.entity.StudentGroup;
import com.example.diploma.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

    private final ProfessorService professorService;

    private  final ScheduleService scheduleService;

    private final StudentService studentService;

    private Professor professor;


    public ProfessorController(ProfessorService professorService, ScheduleService scheduleService, StudentService studentService) {
      this.professorService=professorService;
      this.studentService = studentService;
      this.scheduleService=scheduleService;
    }

    @GetMapping("/classes")
    public String viewClasses(Model model) {
        professor = professorService.getLoggedInProfessor();
        List<Class> classes = professorService.getClassesForProfessor(professor);
        model.addAttribute("classes", classes);
        return "professor/professor-classes";
    }

    @GetMapping("/students/{classId}")
    public String viewStudentsForClass(@PathVariable("classId") int classId, Model model) {

        List<StudentGroup> groups = scheduleService.getGroupsByClass(classId);


        Map<StudentGroup, List<Student>> studentsPerGroup = new HashMap<>();

        for (StudentGroup group : groups) {
            List<Student> students = studentService.getStudentsByGroup(group.getId());
            studentsPerGroup.put(group, students);
        }
        model.addAttribute("groups",groups);
        model.addAttribute("studentsPerGroup", studentsPerGroup);
        model.addAttribute("classId", classId);
        return "professor/class-students";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        professor=professorService.getLoggedInProfessor();
        model.addAttribute("professor", professor);
        return "professor/professor-profile";
    }

}
