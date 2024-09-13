package com.example.diploma.service;

import com.example.diploma.entity.LabAttendance;
import com.example.diploma.entity.LabSchedule;
import com.example.diploma.entity.Student;
import com.example.diploma.repository.LabAttendanceRepository;
import com.example.diploma.repository.LabScheduleRepository;
import com.example.diploma.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LabAttendanceService {

    private final LabAttendanceRepository labAttendanceRepository;

    @Autowired
    public LabAttendanceService(LabAttendanceRepository labAttendanceRepository,
                                StudentRepository studentRepository,
                                LabScheduleRepository labScheduleRepository) {
        this.labAttendanceRepository = labAttendanceRepository;

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

    public Map<String, Object> calculateLabAttendance(int studentId, int classId) {
        List<LabAttendance> labAttendance = labAttendanceRepository.findByClassIdAndStudentId( classId,studentId);
        int totalLabs = labAttendance.size();
        long attendedLabs=0;
        double percentage=0;
        boolean eligible=false;

        if(totalLabs==0){
             percentage =  100;
             eligible =true;
        }
        else {
            attendedLabs = labAttendance.stream().filter(LabAttendance::getAttended).count();
            percentage= (attendedLabs / (double) totalLabs) * 100;
            eligible= percentage == 100;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("attended", attendedLabs);
        result.put("total", totalLabs);
        result.put("percentage", percentage);
        result.put("eligible", eligible);

        return result;
    }

}

