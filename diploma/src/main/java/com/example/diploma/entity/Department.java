package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

}
