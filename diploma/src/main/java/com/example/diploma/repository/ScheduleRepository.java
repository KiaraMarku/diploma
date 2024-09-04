package com.example.diploma.repository;

import com.example.diploma.entity.Class;
import com.example.diploma.entity.Professor;
import com.example.diploma.entity.Schedule;
import com.example.diploma.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Integer> {


    @Query("SELECT DISTINCT s.theClass FROM Schedule s WHERE s.professor = :professor")
    List<Class> findDistinctClassesByProfessor(@Param("professor") Professor professor);
    @Query("SELECT DISTINCT s.theClass FROM Schedule s WHERE s.professor = :professor AND s.type='Seminar'")
    List<Class> findDistinctSeminarsByProfessor(Professor professor);

    List<Schedule> findByTheClassIdAndStudentGroupsId(int classId, int groupId);

    @Query("SELECT DISTINCT g FROM Schedule s JOIN s.studentGroups g WHERE s.theClass.id = :classId")
    List<StudentGroup> findGroupsByClassId(@Param("classId") int classId);

}
