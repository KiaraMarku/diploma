package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "class_result")
@Data
public class StudentClassRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_requirement_id")
    private ClassRequirement classRequirement;

    @Column(name = "score")
    private Integer score; // Stores either the points or a pass/fail score (1 for pass, 0 for fail)
}
