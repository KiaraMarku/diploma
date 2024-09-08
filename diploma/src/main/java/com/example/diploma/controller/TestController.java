package com.example.diploma.controller;

import com.example.diploma.entity.LabAttendance;
import com.example.diploma.entity.StudentGroup;
import com.example.diploma.service.LabAttendanceService;
import com.example.diploma.service.ScheduleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {


    ScheduleService scheduleService;
     LabAttendanceService lab;

    public TestController(LabAttendanceService labrepo) {
        this.lab = labrepo;
    }

    @GetMapping("/groups")
    @ResponseBody
    public List<StudentGroup> getGroups(@RequestParam Integer scholarYearId) {

        return scheduleService.getGroupsByScholarYear(scholarYearId);
    }

    @GetMapping("/labAttendance")
    @ResponseBody
    public List<LabAttendance> getLabs() {

        return lab.getLabAttendanceByClassAndStudent(1,14);
    }
}
