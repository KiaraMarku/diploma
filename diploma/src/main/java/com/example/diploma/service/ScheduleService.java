package com.example.diploma.service;

import com.example.diploma.extra.ScheduleEvent;
import com.example.diploma.entity.Schedule;
import com.example.diploma.entity.ScholarYear;
import com.example.diploma.entity.StudentGroup;
import com.example.diploma.repository.ScheduleRepository;
import com.example.diploma.repository.ScholarYearRepository;
import com.example.diploma.repository.StudentGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScholarYearRepository scholarYearRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    public List<ScholarYear> getAllScholarYears() {
        return scholarYearRepository.findAll();
    }

    public List<StudentGroup> getGroupsByScholarYear( int scholarYearId) {
        return studentGroupRepository.findByScholarYearId( scholarYearId);
    }


    public List<ScheduleEvent> convertToScheduleEvents(Set<Schedule> schedules) {

        schedules.forEach(schedule -> {
            System.out.println("Schedule ID: " + schedule.getId() + ", Class: " + schedule.getTheClass().getName()+" "+schedule.getDayOfWeek());
        });
        List<ScheduleEvent> events = schedules.stream()
                .map(schedule -> new ScheduleEvent(
                        schedule.getTheClass().getName(), // Use the class name as the event title
                        "2024-08-07T" + schedule.getStartTime().toString(),
                        "2024-08-07T" + schedule.getEndTime().toString(),
                        getDayOfWeekNumber(schedule.getDayOfWeek())  ,
                        schedule.getProfessor().getFirstName()+" "+schedule.getProfessor().getLastName(),
                        schedule.getHall(),
                        schedule.getType()
                )).collect(Collectors.toList());
        events.forEach(event -> {
            System.out.println("Event: " + event.getTitle() + ", Day: " + event.getDow());
        });
          return  events;
    }

    public int getDayOfWeekNumber(String dayOfWeek) {
        return switch (dayOfWeek.toLowerCase()) {
            case "monday" -> 1;
            case "tuesday" -> 2;
            case "wednesday" -> 3;
            case "thursday" -> 4;
            case "friday" -> 5;
            case "saturday" -> 6;
            case "sunday" -> 0;
            default -> throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        };
    }

    public List<Schedule> getSchedulesForClassAndGroup(int classId, int groupId) {
        List<Schedule> schedules= scheduleRepository.findByTheClassIdAndStudentGroupsId(classId,groupId);
        List<Schedule> seminars=new ArrayList<>();
        for(Schedule schedule:schedules){
            if(schedule.getType().equals("Seminar"))
                seminars.add(schedule);
        }
        return seminars;
    }

    public List<StudentGroup> getGroupsByClass(int classId) {
        return scheduleRepository.findGroupsByClassId(classId);
    }
}
