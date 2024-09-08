package com.example.diploma.controller;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.Student;
import com.example.diploma.service.AttendanceService;
import com.example.diploma.service.LabAttendanceService;
import com.example.diploma.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;
    private  Student student;
    private AttendanceService seminarAttendanceService;
    private LabAttendanceService labAttendanceService;

    @Autowired
    public StudentController(StudentService studentService, AttendanceService seminarAttendanceService, LabAttendanceService labAttendanceService) {
        this.studentService = studentService;

        this.seminarAttendanceService = seminarAttendanceService;
        this.labAttendanceService = labAttendanceService;
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        this.student=studentService.getLoggedInStudent();
        model.addAttribute("student", student);
        return "student/student-profile";
    }

    @GetMapping("/classes")
    public String viewClasses(Model model) {
        this.student = studentService.getLoggedInStudent();
        Set<Class> classes = studentService.getClassesForStudent(student);
        model.addAttribute("classes", classes);
        return "student/student-classes";
    }

    @GetMapping("/attendance/{classId}")
    public String viewAttendanceSummary(@PathVariable("classId") int classId, Model model) {
        this.student=studentService.getLoggedInStudent();
        // Get seminar attendance details
        Map<String, Object> seminarAttendance = seminarAttendanceService.calculateSeminarAttendance(student.getId(), classId);
        model.addAttribute("seminarAttendance", seminarAttendance);

        // Get lab attendance details
        Map<String, Object> labAttendance = labAttendanceService.calculateLabAttendance(student.getId(), classId);
        model.addAttribute("labAttendance", labAttendance);

        return "student/performance-summary";
    }

    @GetMapping("/{studentId}/attendance/class/{classId}")
    public String viewAttendanceSummary(@PathVariable("classId") int classId,@PathVariable("studentId") int studentId, Model model) {

        // Get seminar attendance details
        Map<String, Object> seminarAttendance = seminarAttendanceService.calculateSeminarAttendance(studentId, classId);
        model.addAttribute("seminarAttendance", seminarAttendance);

        // Get lab attendance details
        Map<String, Object> labAttendance = labAttendanceService.calculateLabAttendance(studentId, classId);
        model.addAttribute("labAttendance", labAttendance);

        return "student/performance-summary";
    }
}

