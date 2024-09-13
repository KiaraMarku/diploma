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

    @Query("SELECT ec FROM ExamCopy ec WHERE ec.exam.id = :examId")
    List<ExamCopy> findByExamId(@Param("examId") int examId);

    @Query("SELECT e FROM ExamCopy e WHERE e.student.id = :studentId AND e.exam.id = :examId")
    Optional<ExamCopy> findByStudentIdAndExamId(@Param("studentId") int studentId, @Param("examId") int examId);


    @Query("SELECT e FROM ExamCopy e WHERE e.student.id = :studentId AND e.grade IS NOT NULL")
    List<ExamCopy> findGradedExamsByStudentId(@Param("studentId") int studentId);


}
