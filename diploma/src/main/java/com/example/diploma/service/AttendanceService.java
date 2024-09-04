package com.example.diploma.service;

import com.example.diploma.entity.Attendance;
import com.example.diploma.entity.Student;
import com.example.diploma.repository.AttendanceRepository;
import com.example.diploma.repository.ScheduleRepository;
import com.example.diploma.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {


    private final AttendanceRepository attendanceRepository;
    private final ScheduleRepository scheduleRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, ScheduleRepository scheduleRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.scheduleRepository = scheduleRepository;
        this.studentRepository = studentRepository;
    }
 public  List<Attendance> getAttendance(int week,int classId,int groupId){
        return attendanceRepository.findByWeekAndClassAndGroup(week, classId, groupId);
 }
    public List<Attendance> processAttendanceData(Map<String, String> attendanceData, int week, int classId, int groupId) throws ParseException, ParseException {
        List<Attendance> attendanceList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (String key : attendanceData.keySet()) {
            if (key.startsWith("attendance_")) {
                String[] parts = key.split("_");
                int studentId = Integer.parseInt(parts[1]);
                int scheduleId = Integer.parseInt(parts[2]);
                System.out.println(attendanceData.get(key));
                boolean attended = Boolean.parseBoolean(attendanceData.get(key));

                String dateKey = "date_" + scheduleId;
                String dateString = attendanceData.get(dateKey);
                Date date = dateFormat.parse(dateString);

                Attendance existingAttendance = attendanceRepository.findByStudentAndScheduleAndWeek(
                        studentRepository.findById(studentId).orElse(null),
                        scheduleRepository.findById(scheduleId).orElse(null),
                        week
                );

                if (existingAttendance != null) {
                    existingAttendance.setAttended(attended);
                    existingAttendance.setDate(date);
                    attendanceRepository.save(existingAttendance);

                } else {
                    Attendance attendance = new Attendance();
                    attendance.setWeek(week);
                    attendance.setStudent(studentRepository.findById(studentId).orElse(null));
                    attendance.setSchedule(scheduleRepository.findById(scheduleId).orElse(null));
                    attendance.setAttended(attended);
                    attendance.setDate(date);
                    attendanceList.add(attendance);
                }
            }
        }
        return attendanceList;
    }


    public void saveAttendanceRecords(List<Attendance> attendances) {
        attendanceRepository.saveAll(attendances);
    }


    public List<Attendance> getAttendanceForStudent(Student student, int classId,int week) {
        return attendanceRepository.findByStudentAndClassAndWeek(student, classId,week);
    }
}



