package com.example.diploma.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "scholar_year")
@Data
public class ScholarYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "year_number")
    private String yearNumber;


}
