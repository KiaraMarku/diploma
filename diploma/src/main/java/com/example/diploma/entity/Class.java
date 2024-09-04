package com.example.diploma.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "class")
@Data
public class Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "has_lab")
    private Boolean hasLab;

    @Column(name = "has_coursework")
    private Boolean hasCoursework;

    @Column(name = "has_midterm")
    private Boolean hasMidterm;

}
