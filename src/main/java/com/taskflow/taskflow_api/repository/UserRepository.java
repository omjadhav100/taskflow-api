package com.taskflow.taskflow_api.repository;

import com.taskflow.taskflow_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// This is the ONLY UserRepository.java in the whole project now -
// having it defined twice in two folders is what caused your
// "duplicate class: UserRepository" error.
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
