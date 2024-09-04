package com.example.diploma.controller;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller

public class LabScheduleController {
    private final GroupService groupService;
    private  final ScheduleService scheduleService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final LabScheduleService labScheduleService;
    private  final ClassService classService;
    private final LabAttendanceService labAttendanceService;

    public LabScheduleController(GroupService groupService, ScheduleService scheduleService, ProfessorService professorService, StudentService studentService, LabScheduleService labScheduleService, ClassService classService, LabAttendanceService labAttendanceService) {
        this.groupService = groupService;
        this.scheduleService = scheduleService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.labScheduleService = labScheduleService;
        this.classService = classService;
        this.labAttendanceService = labAttendanceService;
    }

    @GetMapping("/professor/labSchedules")
    public String viewLabSchedulesForProfesor( Model model) {
        Professor professor = professorService.getLoggedInProfessor();
        List<Class> classes = professorService.getClassesForProfessor(professor);
        Map<Integer, List<LabSchedule>> labSchedulesByClass = new HashMap<>();

        for (Class cls : classes) {
            List<LabSchedule> labSchedules = labScheduleService.getLabSchedulesByClass(cls.getId());
            labSchedulesByClass.put(cls.getId(), labSchedules);
        }

        model.addAttribute("classes", classes);
        model.addAttribute("labSchedulesByClass", labSchedulesByClass);

        return "lab-schedules";
    }

    @GetMapping("/student/labSchedules")
    public String viewLabSchedulesForStudent( Model model) {
        Student student = studentService.getLoggedInStudent();
        Set<Class> classes = studentService.getClassesForStudent(student);
        Map<Integer, List<LabSchedule>> labSchedulesByClass = new HashMap<>();
        Map<Integer, List<LabAttendance>> labAttendanceByClass = new HashMap<>();

        for (Class cls : classes) {
            List<LabSchedule> labSchedules = labScheduleService.getLabSchedulesByClass(cls.getId());

            List<LabAttendance> labAttendances = labAttendanceService.getLabAttendanceByClassAndStudent(cls.getId(), student.getId());
            labSchedulesByClass.put(cls.getId(), labSchedules);
            labAttendanceByClass.put(cls.getId(), labAttendances);
        }

        model.addAttribute("classes", classes);
        model.addAttribute("labSchedulesByClass", labSchedulesByClass);
        model.addAttribute("labAttendanceByClass", labAttendanceByClass);

        return "lab-schedules-student";
    }

    @GetMapping("/professor/class/{id}/addLabSchedule")
    public String showAddLabScheduleForm(@PathVariable("id") int classId, Model model) {
        LabSchedule labSchedule = new LabSchedule();
        List<StudentGroup> groups = scheduleService.getGroupsByClass(classId);;
        model.addAttribute("classId",classId);
        model.addAttribute("labSchedule", labSchedule);
        model.addAttribute("groups",groups);
        return "add-lab-schedule";
    }

    @PostMapping("/professor/class/{id}/addLabSchedule")
    public String addLabSchedule(@ModelAttribute("labSchedule") LabSchedule labSchedule, @RequestParam("groupId") int groupId, @PathVariable("id") int classId) {
        labSchedule.setTheClass(classService.findById(classId));
        labSchedule.setStudentGroup(groupService.findById(groupId));
        labScheduleService.saveLabSchedule(labSchedule);
        return "redirect:/professor/class/" + labSchedule.getTheClass().getId();
    }

}
