package com.example.diploma.service;

import com.example.diploma.entity.StudentGroup;
import com.example.diploma.repository.StudentGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    StudentGroupRepository studentGroupRepository;
    private Object examCopyRepository;

    public GroupService(StudentGroupRepository studentGroupRepository){
        this.studentGroupRepository=studentGroupRepository;
    }

   public List<StudentGroup> findAll(){
        return studentGroupRepository.findAll();
   }

    public StudentGroup findById(Integer id){

        return studentGroupRepository.findById(id).orElse(null);
    }



}
