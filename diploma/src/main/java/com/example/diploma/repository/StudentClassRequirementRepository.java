package com.example.diploma.repository;

import com.example.diploma.entity.StudentClassRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentClassRequirementRepository extends JpaRepository<StudentClassRequirement,Integer> {
    Optional<StudentClassRequirement> findByStudentIdAndClassRequirementId(int studentId, int classRequirementId);

    @Query("SELECT scr FROM StudentClassRequirement scr WHERE scr.student.id = :studentId AND scr.classRequirement.theClass.id = :classId")
    List<StudentClassRequirement> findByStudentIdAndClassId(@Param("studentId") int studentId, @Param("classId") int classId);
}
