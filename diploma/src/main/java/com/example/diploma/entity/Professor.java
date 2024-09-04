package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "professor")
@Data
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "major")
    private String major;

    @Column(name = "title")
    private String title;

    @Column(name = "birth_date")
    private Date birthDate;

    private String gender;

    @Column(name = "card_id")
    private String cardId;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

}
