package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.entity.Board;
import com.taskflow.taskflow_api.entity.TaskList;
import com.taskflow.taskflow_api.exception.BoardNotFoundException;
import com.taskflow.taskflow_api.repository.BoardRepository;
import com.taskflow.taskflow_api.repository.TaskListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskListService {

    private final TaskListRepository taskListRepository;
    private final BoardRepository boardRepository;

    public TaskListService(TaskListRepository taskListRepository, BoardRepository boardRepository) {
        this.taskListRepository = taskListRepository;
        this.boardRepository = boardRepository;
    }

    public TaskList createList(Long boardId, String name) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));

        TaskList list = new TaskList();
        list.setName(name);
        list.setBoard(board);
        return taskListRepository.save(list);
    }

    public List<TaskList> getListsForBoard(Long boardId) {
        return taskListRepository.findByBoardId(boardId);
    }
}
