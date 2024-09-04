package com.example.diploma.controller;

import com.example.diploma.extra.ScheduleEvent;
import com.example.diploma.entity.StudentGroup;
import com.example.diploma.service.GroupService;
import com.example.diploma.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {


    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private GroupService groupService;

    @GetMapping
    public String showScheduleForm(Model model) {
        model.addAttribute("scholarYears", scheduleService.getAllScholarYears());
        model.addAttribute("groups", new ArrayList<StudentGroup>()); // Initially empty
        return "schedule";
    }


    @GetMapping("/groups")
    @ResponseBody
    public List<StudentGroup> getGroups(@RequestParam Integer scholarYearId) {
        System.out.println(scheduleService.getGroupsByScholarYear(scholarYearId));
        return scheduleService.getGroupsByScholarYear(scholarYearId);

    }

    @GetMapping("/query")
    @ResponseBody
    public List<ScheduleEvent> getSchedule(@RequestParam Integer groupId) {
        StudentGroup group = groupService.findById(groupId);

        return scheduleService.convertToScheduleEvents(group.getSchedules());
    }



}



