package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String message;

    @Column(name = "is_read")
    private boolean read = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
