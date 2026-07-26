package com.taskflow.taskflow_api.repository;

import com.taskflow.taskflow_api.entity.Board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// No @Repository needed - Spring Data JPA detects JpaRepository subinterfaces automatically.
// No implementation needed either - Spring generates it at runtime.
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByUserId(Long userId);
}
