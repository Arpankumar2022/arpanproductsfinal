package com.arpanbags.products.arpanbagsproducts.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String mobileNumber;

    private String password;
    private String address;

    private String companyName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // Join table
            joinColumns = @JoinColumn(name = "user_id"), // FK to user
            inverseJoinColumns = @JoinColumn(name = "role_id") // FK to role
    )
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Status status = Status.INACTIVE;

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters and setters

    public enum Status {
        ACTIVE, INACTIVE
    }
}
