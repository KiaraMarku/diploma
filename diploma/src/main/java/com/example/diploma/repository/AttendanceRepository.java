package com.example.diploma.repository;

import com.example.diploma.entity.Attendance;
import com.example.diploma.entity.Schedule;
import com.example.diploma.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance,Integer> {
    @Query("SELECT a FROM Attendance a JOIN a.schedule s JOIN s.studentGroups g WHERE a.week = :week AND s.theClass.id = :classId AND g.id = :groupId ")
    List<Attendance> findByWeekAndClassAndGroup(@Param("week") int week, @Param("classId") int classId, @Param("groupId") int groupId);

    Attendance findByStudentAndScheduleAndWeek(Student student, Schedule schedule, int week);

    @Query("SELECT a FROM Attendance a JOIN a.schedule s WHERE  s.theClass.id = :classId AND a.week=:week AND a.student = :student ")
    List<Attendance> findByStudentAndClassAndWeek(Student student, int classId,int week);

    @Query("SELECT a FROM Attendance a WHERE a.student.id = :studentId AND a.schedule.theClass.id = :classId AND a.schedule.type = 'SEMINAR'")
    List<Attendance> findByStudentAndClass(@Param("studentId") int studentId, @Param("classId") int classId);
}
