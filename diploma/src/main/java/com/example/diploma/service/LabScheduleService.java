package com.example.diploma.service;

import com.example.diploma.entity.LabSchedule;
import com.example.diploma.repository.LabScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
    public class LabScheduleService {

        private final LabScheduleRepository labScheduleRepository;


        public LabScheduleService(LabScheduleRepository labScheduleRepository) {
            this.labScheduleRepository = labScheduleRepository;
        }

        public void saveLabSchedule(LabSchedule labSchedule) {
            labScheduleRepository.save(labSchedule);
        }

        public List<LabSchedule> getLabSchedulesByClass(int classId) {
            return labScheduleRepository.findByTheClassId(classId);
        }

        public LabSchedule findById(int labScheduleId) {
                return labScheduleRepository.findById(labScheduleId);
        }

    public void deleteById(int id) {
            labScheduleRepository.deleteById(id);
    }
}


