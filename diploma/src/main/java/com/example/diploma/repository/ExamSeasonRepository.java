package com.example.diploma.repository;

import com.example.diploma.entity.ExamSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamSeasonRepository extends JpaRepository<ExamSeason, Integer> {
}
