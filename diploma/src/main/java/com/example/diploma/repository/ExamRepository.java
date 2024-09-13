package com.example.diploma.repository;

import com.example.diploma.entity.Exam;
import com.example.diploma.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {
    List<Exam> findByDate(LocalDate date);
    List<Exam> findByExamSeasonIdOrderByDate(int seasonId);

    boolean existsByTheClassIdAndExamSeasonId(int classId, int seasonId);

    @Query("SELECT e FROM Exam e JOIN e.theClass c JOIN Schedule s ON s.theClass.id = c.id " +
            "WHERE s.professor.id = :professorId AND e.date < :currentDate")
    List<Exam> findByProfessorIdAndDateBefore(@Param("professorId") int professorId, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT s FROM Student s JOIN ExamCopy ec ON ec.student.id = s.id WHERE ec.exam.id = :examId")
    List<Student> findStudentsForExam(@Param("examId") int examId);
}
