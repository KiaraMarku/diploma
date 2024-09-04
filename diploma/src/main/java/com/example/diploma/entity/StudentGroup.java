package com.example.diploma.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
    @Table(name = "student_group")
    @Getter
    @Setter
    public class StudentGroup {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        private Integer id;

        private String name;
        @Column(name = "year_level")
        private Integer yearLevel;

        @ManyToOne
        @JoinColumn(name = "scholar_year_id")
        private ScholarYear scholarYear;

        @ManyToOne
        @JoinColumn(name = "department_id")
        private Department department;

        @JsonIgnore
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "schedule_group",
                joinColumns = @JoinColumn(name ="student_group_id" ),
                inverseJoinColumns = @JoinColumn(name ="schedule_id" )
        )
        private Set<Schedule> schedules;

        @JsonIgnore
        @OneToMany(mappedBy = "studentGroup")
        private List<Student> students;


    }


