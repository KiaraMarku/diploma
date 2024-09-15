package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_class_grade")
@Data
public class StudentClassGrade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class theClass;

    @Column(name = "final_grade", nullable = false)
    private double finalGrade;
    @Column(name = "final_score", nullable = false)
    private int finalScore;

    @Column(name = "status", nullable = false)
    private String status;
}
