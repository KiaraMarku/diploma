package com.example.diploma.repository;

import com.example.diploma.entity.ExamCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamCopyRepository extends JpaRepository<ExamCopy, Integer> {

    Optional<ExamCopy> findByStudentIdAndExamId(@Param("studentId") int studentId, @Param("examId") int examId);

    @Query("SELECT e FROM ExamCopy e WHERE e.student.id = :studentId AND e.grade IS NOT NULL")
    List<ExamCopy> findGradedExamsByStudentId(@Param("studentId") int studentId);

    @Query("SELECT ec FROM ExamCopy ec WHERE ec.student.id = :studentId AND ec.exam.theClass.id = :classId")
    ExamCopy findByStudentIdAndClassId(@Param("studentId") int studentId, @Param("classId") int classId);



}
