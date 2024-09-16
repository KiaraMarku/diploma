package com.example.diploma.repository;

import com.example.diploma.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    Student findByUsername(String username);
    List<Student> findStudentsByStudentGroupId(Integer studentGroup_id);
}
