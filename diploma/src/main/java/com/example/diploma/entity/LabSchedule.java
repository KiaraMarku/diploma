package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "lab_schedule")
@Data
public class LabSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class theClass;

    @OneToOne
    @JoinColumn(name = "student_group_id")
    private StudentGroup studentGroup;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private String hall;

    @OneToMany(mappedBy = "labSchedule", cascade = CascadeType.ALL)
    List <LabAttendance> labAttendances;
}

