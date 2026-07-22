package com.taskflow.taskflow_api.exception;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException(Long boardId) {
        super("Board not found with id: " + boardId);
    }
}
