package com.example.diploma.service;

import com.example.diploma.entity.Department;
import com.example.diploma.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private DepartmentRepository departmenRepository;

    public DepartmentService(DepartmentRepository departmenRepository) {
        this.departmenRepository = departmenRepository;
    }
    public List<Department> findAll(){
        return departmenRepository.findAll();

    }
}
