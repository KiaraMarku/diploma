package com.example.diploma.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "exam_copy")
@Data
public class ExamCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "data")
    private byte[] data;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "score")
    private Integer score;

    @Column(name = "grade")
    private String grade;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

}

