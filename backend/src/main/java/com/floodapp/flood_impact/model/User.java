package com.floodapp.flood_impact.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    // --- NEW FIELDS FOR VERIFICATION ---
    private boolean enabled = false; // Default to false (not verified)
    private String verificationToken;
}