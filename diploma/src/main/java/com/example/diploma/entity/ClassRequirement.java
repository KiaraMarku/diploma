package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "class_requirement")
@Data
public class ClassRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class theClass;

    @Column(name = "requirement_type") // Either "lab", "coursework", "midterm"
    private String requirementType;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "pass_fail")
    private Boolean passFail; // True if it's pass/fail, false if it's graded


}
