package com.taskflow.taskflow_api.repository;

import com.taskflow.taskflow_api.entity.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    // "findByBoardId" works via derived query naming even though the field
    // on TaskList is called "board" (a Board object), because Hibernate
    // knows to look at board.id automatically for this naming pattern.
    List<TaskList> findByBoardId(Long boardId);
}
