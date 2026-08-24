package com.rdavies.authservice.repositories;

import com.rdavies.authservice.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
