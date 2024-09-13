package com.example.diploma.repository;

import com.example.diploma.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Integer> {
    Professor findByUsername(String username);

    List<Professor> findProfessorsByDepartmentId(int departmentId);
}
