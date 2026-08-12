package com.taskflow.taskflow_api.repository;

import com.taskflow.taskflow_api.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByBoardId(Long boardId);
}
