package com.example.diploma.service;

import com.example.diploma.entity.Department;
import com.example.diploma.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmenRepository) {
        this.departmentRepository = departmenRepository;
    }
    public List<Department> findAll(){
        return departmentRepository.findAll();

    }
}
