package com.example.diploma.service;


import com.example.diploma.entity.Class;
import com.example.diploma.entity.Professor;
import com.example.diploma.repository.ProfessorRepository;
import com.example.diploma.repository.ScheduleRepository;
import com.example.diploma.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProfessorService {

    private ProfessorRepository professorRepository;
    private ScheduleRepository scheduleRepository;
    public ProfessorService(){

    }
     @Autowired
    public ProfessorService(ProfessorRepository professorRepository, ScheduleRepository scheduleRepository) {
        this.professorRepository = professorRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<Class> getClassesForProfessor(Professor professor) {

        return scheduleRepository.findDistinctClassesByProfessor(professor);
    }

    public List<Class> getSeminarsForProfessor(Professor professor) {

        return scheduleRepository.findDistinctSeminarsByProfessor(professor);
    }


    public Professor getLoggedInProfessor() {
        String username = SecurityUtils.getCurrentUsername();
        return professorRepository.findByUsername(username);
    }

}
