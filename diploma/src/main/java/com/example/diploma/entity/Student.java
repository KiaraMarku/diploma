package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "student")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    @Column(name = "birth_date")
    private Date birthDate;

    private String gender;

    @Column(name = "card_id")
    private String cardId;

    @ManyToOne
    @JoinColumn(name = "student_group_id")
    private StudentGroup studentGroup;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private Set<StudentGroupHistory> groupHistory;
}
