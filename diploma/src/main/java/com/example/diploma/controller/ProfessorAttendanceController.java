package com.example.diploma.controller;


import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/professor/attendance")
public class ProfessorAttendanceController {

    private final AttendanceService attendanceService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final ScheduleService scheduleService;
    private final GroupService groupService;
    private  final ClassService classService;
    private final  LabAttendanceService labAttendanceService;


    @Autowired
    public ProfessorAttendanceController(AttendanceService attendanceService, ProfessorService professorService, StudentService studentService, ScheduleService scheduleService, GroupService groupService, ClassService classService, LabAttendanceService labAttendanceService) {
        this.attendanceService = attendanceService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
        this.groupService = groupService;
        this.classService = classService;
        this.labAttendanceService = labAttendanceService;
    }

    @GetMapping("/classes")
    public String selectClass(Model model) {
        Professor professor = professorService.getLoggedInProfessor();
        List<Class> classes = professorService.getSeminarsForProfessor(professor);
        model.addAttribute("classes", classes);
        return "professor/attendance-form";
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<StudentGroup> getGroupsForClass(@RequestParam("classId") int classId) {
        return scheduleService.getGroupsByClass(classId);
    }


    @GetMapping("/mark")
    public String markAttendance(@RequestParam("week") int week,@RequestParam("classId") int classId, @RequestParam("groupId") int groupId, Model model) {
        List<Schedule> schedules = scheduleService.getSchedulesForClassAndGroup(classId, groupId);
        List<Student> students = studentService.getStudentsByGroup(groupId);

        List<Attendance> existingAttendance = attendanceService.getAttendance(week, classId, groupId);

        Map<String, Boolean> attendanceMap = new HashMap<>();
        Map<Integer, Date> dateMap = new HashMap<>();
        for (Attendance attendance : existingAttendance) {
            String key = attendance.getStudent().getId() + "_" + attendance.getSchedule().getId();
            attendanceMap.put(key, attendance.getAttended());
            dateMap.put(attendance.getSchedule().getId(),attendance.getDate());
        }

        StudentGroup group=groupService.findById(groupId);
        Class theClass=classService.findById(classId);

        model.addAttribute("schedules", schedules);
        model.addAttribute("students", students);
        model.addAttribute("class", theClass);
        model.addAttribute("group", group);
        model.addAttribute("week",week);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("dateMap", dateMap);
        return "professor/mark-attendance";
    }

    @PostMapping("/mark")
    public String saveAttendance(@RequestParam Map<String, String> attendanceData,
                                 @RequestParam("week") int week,
                                 @RequestParam("classId") int classId,
                                 @RequestParam("groupId") int groupId) throws ParseException {
        List<Attendance> attendanceList = attendanceService.processAttendanceData(attendanceData, week, classId, groupId);
        attendanceService.saveAttendanceRecords(attendanceList);
        return "redirect:/professor/attendance/classes";
    }

    @GetMapping("/class/{classId}/student/{studentId}/attendance")
    public String viewStudentAttendance(@PathVariable("classId") int classId, @PathVariable("studentId") int studentId, Model model) {
        Map<String, Object> seminarAttendance = attendanceService.calculateSeminarAttendance(studentId, classId);
        Map<String, Object> labAttendance = labAttendanceService.calculateLabAttendance(studentId, classId);

        model.addAttribute("seminarAttendance", seminarAttendance);
        model.addAttribute("labAttendance", labAttendance);
        return "student-attendance-details";
    }


}
