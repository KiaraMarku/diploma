package com.example.diploma.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "exam")
@Data
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "season")
    private String season;

    @Column(name = "date")
    private Date date;

    @Column(name = "exam_hall")
    private String examHall;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class aClass;

    @ManyToOne
    @JoinColumn(name = "scholar_year_id")
    private ScholarYear scholarYear;


}

