package com.example.diploma.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "exam")
@Data
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "exam_season_id")
    private ExamSeason examSeason;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "exam_hall")
    private String examHall;
    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @ManyToOne
    @JsonProperty("class")
    @JoinColumn(name = "class_id")
    private Class theClass;

}

