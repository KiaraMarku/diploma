package com.example.diploma.service;

import com.example.diploma.entity.Class;
import com.example.diploma.repository.ClassRepository;
import org.springframework.stereotype.Service;

@Service
public class ClassService {
    private ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public Class findById(Integer id){
        return classRepository.findById(id).orElse(null);
    }



}
