package com.example.diploma.repository;

import com.example.diploma.entity.LabSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface LabScheduleRepository extends JpaRepository<LabSchedule, Integer> {

    List<LabSchedule> findByTheClassId(int classId);

    LabSchedule findById(int labScheduleId);
}

