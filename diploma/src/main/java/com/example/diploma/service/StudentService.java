package com.example.diploma.service;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.*;
import com.example.diploma.repository.*;
import com.example.diploma.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    private StudentRepository studentRepository;
    private StudentGroupRepository studentGroupRepository;
    private StudentGroupHistoryRepository studentGroupHistoryRepository;
    private StudentClassGradeRepository studentClassGradeRepository;

    public StudentService(){

    }
    @Autowired
    public StudentService(StudentRepository studentRepository, StudentGroupRepository studentGroupRepository, ScheduleRepository scheduleRepository, StudentGroupHistoryRepository studentGroupHistoryRepository, StudentClassGradeRepository studentClassGradeRepository) {
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.studentGroupHistoryRepository = studentGroupHistoryRepository;
        this.studentClassGradeRepository = studentClassGradeRepository;
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
        Student existingStudent = studentRepository.findById(student.getId()).orElse(null);

        // If the student exists and the group has changed, update the history
        if (existingStudent != null && !existingStudent.getStudentGroup().equals(student.getStudentGroup())) {

            StudentGroupHistory history = new StudentGroupHistory();
            history.setStudent(student);
            history.setStudentGroup(student.getStudentGroup());
            history.setDate(new Date());

            studentGroupHistoryRepository.save(history);
        }
        studentRepository.save(student);
    }


    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    public Optional<StudentClassGrade> getFinalGradeForClass(int studentId, int classId) {
        return studentClassGradeRepository.findByStudentIdAndTheClassId(studentId, classId);
    }

    public List<StudentClassGrade> getPassedClassesForStudent(int studentId) {
        return studentClassGradeRepository.findByStudentIdAndStatus(studentId, "Passed");
    }

    public double calculateAverageGrade(List<StudentClassGrade> passedClasses) {
        int totalCredits = 0;
        double weightedGradeSum = 0;

        for (StudentClassGrade classGrade : passedClasses) {
            int credits = classGrade.getTheClass().getCredits();
            totalCredits += credits;
            weightedGradeSum += classGrade.getFinalGrade() * credits;
        }

        return totalCredits == 0 ? 0 : weightedGradeSum / totalCredits;
    }

}
