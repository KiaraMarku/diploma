package com.example.diploma.repository;

import com.example.diploma.entity.ClassRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRequirementRepository extends JpaRepository<ClassRequirement,Integer> {
    List<ClassRequirement> findByTheClassId(int classId);
}
