package com.example.diploma.controller;

import com.example.diploma.entity.LabAttendance;
import com.example.diploma.entity.LabSchedule;
import com.example.diploma.entity.Student;
import com.example.diploma.service.LabAttendanceService;
import com.example.diploma.service.LabScheduleService;
import com.example.diploma.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LabAttendanceController {

    private final LabAttendanceService labAttendanceService;
    private final LabScheduleService labScheduleService;
    private final StudentService studentService;

    @Autowired
    public LabAttendanceController(LabAttendanceService labAttendanceService,
                                   LabScheduleService labScheduleService,
                                   StudentService studentService) {
        this.labAttendanceService = labAttendanceService;
        this.labScheduleService = labScheduleService;
        this.studentService = studentService;
    }

    @GetMapping("/professor/labs/attendance/{labScheduleId}")
    public String viewLabAttendance(@PathVariable int labScheduleId, Model model) {
        LabSchedule labSchedule = labScheduleService.findById(labScheduleId);
        List<Student> students = studentService.getStudentsByGroup(labSchedule.getStudentGroup().getId());
        List<LabAttendance> labAttendances = labAttendanceService.getLabAttendanceForSchedule(labSchedule);


        Map<Integer, Boolean> attendanceMap = new HashMap<>();
        for (LabAttendance attendance : labAttendances) {
            attendanceMap.put(attendance.getStudent().getId(), attendance.getAttended());
        }

        model.addAttribute("labSchedule", labSchedule);
        model.addAttribute("students", students);
        model.addAttribute("attendanceMap", attendanceMap);

        return "professor/lab/lab-attendance";
    }

    @PostMapping("/professor/labs/attendance/save")
    public String saveLabAttendance(@RequestParam("labScheduleId") int labScheduleId,
                                    @RequestParam Map<String, String> attendanceData) {
        LabSchedule labSchedule = labScheduleService.findById(labScheduleId);

        for (Student student : labSchedule.getStudentGroup().getStudents()) {
            String attendedKey = "attendance_" + student.getId();
            boolean attended = Boolean.parseBoolean(attendanceData.get(attendedKey));

            LabAttendance labAttendance = labAttendanceService.getLabAttendanceForStudentAndSchedule(student, labSchedule);

            if(labAttendance==null)
                labAttendance=new LabAttendance();

            labAttendance.setStudent(student);
            labAttendance.setLabSchedule(labSchedule);
            labAttendance.setAttended(attended);

            labAttendanceService.saveLabAttendance(labAttendance);
        }

        return "redirect:/professor/labSchedules";
    }



}

