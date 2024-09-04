package com.example.diploma.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Time;
import java.util.Set;

@Entity
@Table(name = "schedule")
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    private String hall;

    private Integer semester;
    @Column(name = "class_type")
    private String type;
    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class theClass;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToMany
    @JoinTable(
            name = "schedule_group",
            joinColumns = @JoinColumn(name = "schedule_id"),
            inverseJoinColumns = @JoinColumn(name = "student_group_id")
    )
    private Set<StudentGroup> studentGroups;

}
