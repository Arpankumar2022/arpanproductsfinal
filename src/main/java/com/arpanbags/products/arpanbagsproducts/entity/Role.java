package com.arpanbags.products.arpanbagsproducts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    private String id;

    private String name; // e.g. "ROLE_USER"
}

