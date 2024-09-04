package com.example.diploma.service;

import com.example.diploma.entity.LabAttendance;
import com.example.diploma.entity.LabSchedule;
import com.example.diploma.entity.Student;
import com.example.diploma.repository.LabAttendanceRepository;
import com.example.diploma.repository.LabScheduleRepository;
import com.example.diploma.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabAttendanceService {

    private final LabAttendanceRepository labAttendanceRepository;
    private final StudentRepository studentRepository;
    private final LabScheduleRepository labScheduleRepository;

    @Autowired
    public LabAttendanceService(LabAttendanceRepository labAttendanceRepository,
                                StudentRepository studentRepository,
                                LabScheduleRepository labScheduleRepository) {
        this.labAttendanceRepository = labAttendanceRepository;
        this.studentRepository = studentRepository;
        this.labScheduleRepository = labScheduleRepository;
    }

    public void saveLabAttendance(LabAttendance labAttendance) {
        labAttendanceRepository.save(labAttendance);
    }

    public List<LabAttendance> getLabAttendanceForSchedule(LabSchedule labSchedule) {
        return labAttendanceRepository.findByLabSchedule(labSchedule);
    }

    public LabAttendance getLabAttendanceForStudentAndSchedule(Student student, LabSchedule labSchedule) {
        return labAttendanceRepository.findByStudentAndLabSchedule(student, labSchedule);
    }

    public List<LabAttendance> getLabAttendanceByClassAndStudent(int classId, int studentId) {
        return labAttendanceRepository.findByClassIdAndStudentId(classId, studentId);
    }

}

