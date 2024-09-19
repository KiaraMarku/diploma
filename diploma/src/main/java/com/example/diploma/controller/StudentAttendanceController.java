package com.example.diploma.controller;

import com.example.diploma.entity.Attendance;
import com.example.diploma.entity.Class;
import com.example.diploma.entity.Student;
import com.example.diploma.service.AttendanceService;
import com.example.diploma.service.ClassService;
import com.example.diploma.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("student/attendance")
public class StudentAttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private ClassService classService;


    public StudentAttendanceController(AttendanceService attendanceService, StudentService studentService, ClassService classService) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.classService = classService;
    }

    @GetMapping("/classes")
    public String viewAttendance(Model model) {
        Student student = studentService.getLoggedInStudent();
        Set<Class> classes = studentService.getClassesForStudent(student);
        model.addAttribute("classes", classes);
        return "student/student-attendance";
    }

    @GetMapping("/records")
    public String getAttendanceRecords(@RequestParam("classId") int classId, @RequestParam("week") int week, Model model) {
        Student student = studentService.getLoggedInStudent();
        Class theClass = classService.findById(classId);
        List<Attendance> attendanceList = attendanceService.getAttendanceForStudent(student, classId, week);

        model.addAttribute("class", theClass);
        model.addAttribute("week", week);
        model.addAttribute("attendanceList", attendanceList);
        return "student/student-attendance-records";
    }


}

