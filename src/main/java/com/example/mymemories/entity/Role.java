package com.example.mymemories.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;


@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ← Long, not UUID

    @Column(unique = true, nullable = false)
    private String name;

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}