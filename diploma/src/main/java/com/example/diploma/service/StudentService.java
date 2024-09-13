package com.example.diploma.service;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.repository.ScheduleRepository;
import com.example.diploma.repository.StudentGroupHistoryRepository;
import com.example.diploma.repository.StudentGroupRepository;
import com.example.diploma.repository.StudentRepository;
import com.example.diploma.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentService {


    private StudentRepository studentRepository;

    private StudentGroupRepository studentGroupRepository;

    private StudentGroupHistoryRepository studentGroupHistoryRepository;

    public StudentService(){

    }
    @Autowired
    public StudentService(StudentRepository studentRepository, StudentGroupRepository studentGroupRepository, ScheduleRepository scheduleRepository, StudentGroupHistoryRepository studentGroupHistoryRepository) {
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.studentGroupHistoryRepository = studentGroupHistoryRepository;
    }

    public void changeStudentGroup(Integer studentId, Integer newGroupId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        StudentGroup currentGroup = student.getStudentGroup();

        if (currentGroup != null) {
            // Record current group in history
            StudentGroupHistory history = new StudentGroupHistory();
            history.setStudent(student);
            history.setStudentGroup(currentGroup);
            history.setDate(new Date()); // Now
            studentGroupHistoryRepository.save(history);
        }


        StudentGroup newGroup = studentGroupRepository.findById(newGroupId).orElseThrow(() -> new RuntimeException("Group not found"));
        student.setStudentGroup(newGroup);
        studentRepository.save(student);
    }


    public Set<Class> getClassesForStudent(Student student) {
        StudentGroup group=student.getStudentGroup();
        Set<Class>  classes=new HashSet<>();
        for (Schedule schedule:group.getSchedules()) {
            classes.add(schedule.getTheClass());
        }
        return classes;
    }

    public Student getLoggedInStudent() {
        String username = SecurityUtils.getCurrentUsername();
        return studentRepository.findByUsername(username);
    }

    public List<Student> getStudentsByGroup(int groupId) {
        return studentRepository.findStudentsByStudentGroupId(groupId);
    }

    public Student getStudentById(int studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }


    public void saveOrUpdateStudent(Student student) {
        studentRepository.save(student);
    }


    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }
}
