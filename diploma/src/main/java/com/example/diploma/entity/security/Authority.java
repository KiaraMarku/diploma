package com.example.diploma.entity.security;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "authorities")
@Data

public class Authority {
    @Id
    @ManyToOne
    @JoinColumn(name = "username")
    private User username;

    @Id
    private String authority;

    public Authority() {

    }

    public Authority(User username, String authority) {
        this.username = username;
        this.authority = authority;
    }
}
