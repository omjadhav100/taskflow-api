package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.entity.Board;
import com.taskflow.taskflow_api.entity.TaskList;
import com.taskflow.taskflow_api.exception.BoardNotFoundException;
import com.taskflow.taskflow_api.exception.TaskListNotFoundException;
import com.taskflow.taskflow_api.exception.UnauthorizedAccessException;
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

    public TaskList updateList(Long listId, String newName, Long currentUserId) {
        TaskList list = taskListRepository.findById(listId)
                .orElseThrow(() -> new TaskListNotFoundException(listId));

        if (!list.getBoard().getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You do not have permission to edit this list");
        }

        list.setName(newName);
        return taskListRepository.save(list);
    }

    public void deleteList(Long listId, Long currentUserId) {
        TaskList list = taskListRepository.findById(listId)
                .orElseThrow(() -> new TaskListNotFoundException(listId));

        if (!list.getBoard().getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this list");
        }

        taskListRepository.deleteById(listId);
    }
}
