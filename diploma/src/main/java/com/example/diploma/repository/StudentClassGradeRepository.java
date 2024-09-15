package com.example.diploma.repository;

import com.example.diploma.entity.StudentClassGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentClassGradeRepository extends JpaRepository<StudentClassGrade, Integer> {

    Optional<StudentClassGrade> findByStudentIdAndTheClassId(@Param("studentId") int studentId, @Param("classId") int classId);
    List<StudentClassGrade> findByStudentIdAndStatus(@Param("studentId") int studentId, @Param("status") String status);
}
