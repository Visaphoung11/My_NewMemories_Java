package com.example.mymemories.entity;

import jakarta.persistence.*;

@Entity
@Table ( name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer RoleId;

    @Column(nullable = false, unique = true)
    private String name;

}
