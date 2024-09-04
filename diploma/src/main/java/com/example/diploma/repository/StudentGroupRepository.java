package com.example.diploma.repository;

import com.example.diploma.entity.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentGroupRepository extends JpaRepository<StudentGroup,Integer> {

    List<StudentGroup> findByScholarYearId(int scholarYearId);


}
