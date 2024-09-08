package com.example.diploma.repository;

import com.example.diploma.entity.LabAttendance;
import com.example.diploma.entity.LabSchedule;
import com.example.diploma.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LabAttendanceRepository extends JpaRepository<LabAttendance, Integer> {
    List<LabAttendance> findByLabSchedule(LabSchedule labSchedule);
    LabAttendance findByStudentAndLabSchedule(Student student, LabSchedule labSchedule);

    @Query("SELECT la FROM LabAttendance la WHERE la.student.id = :studentId AND la.labSchedule.theClass.id = :classId")
    List<LabAttendance> findByClassIdAndStudentId(@Param("classId") int classId, @Param("studentId") int studentId);


}

