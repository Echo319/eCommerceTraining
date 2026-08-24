package com.rdavies.authservice.repositories;

import com.rdavies.authservice.model.dao.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
