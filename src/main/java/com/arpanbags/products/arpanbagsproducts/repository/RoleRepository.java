package com.arpanbags.products.arpanbagsproducts.repository;

import com.arpanbags.products.arpanbagsproducts.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleName);
}
