package com.taskflow.taskflow_api.exception;

public class TaskListNotFoundException extends RuntimeException {
    public TaskListNotFoundException(Long listId) {
        super("Task list not found with id: " + listId);
    }
}
