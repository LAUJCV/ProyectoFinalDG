package com.laura.easyflights.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.laura.easyflights.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
}
