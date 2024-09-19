package com.example.diploma.service;

import com.example.diploma.entity.Class;
import com.example.diploma.repository.ClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassService {
    private final ClassRepository classRepository;

    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    public Class findById(Integer id) {
        return classRepository.findById(id).orElse(null);
    }


    public Class getClassById(int id) {
        return classRepository.findById(id).orElse(null);
    }

    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }
}
