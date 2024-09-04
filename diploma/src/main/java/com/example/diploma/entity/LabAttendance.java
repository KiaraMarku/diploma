package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "lab_attendance")
public class LabAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lab_schedule_id")
    private LabSchedule labSchedule;

    private Boolean attended;

}
