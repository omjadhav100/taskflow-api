package com.taskflow.taskflow_api.controller;

import com.taskflow.taskflow_api.dto.CreateBoardRequest;
import com.taskflow.taskflow_api.entity.Task;
import com.taskflow.taskflow_api.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lists/{listId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getTasks(@PathVariable Long listId) {
        return taskService.getTasksForList(listId);
    }

    @PostMapping
    public Task createTask(@PathVariable Long listId,
                            @RequestBody CreateBoardRequest request) {
        // Reusing CreateBoardRequest's "title" field shape here is a shortcut -
        // consider making a dedicated CreateTaskRequest DTO once you add more Task fields.
        return taskService.createTask(listId, request.getTitle());
    }
}
