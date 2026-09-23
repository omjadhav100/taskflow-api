package com.taskflow.taskflow_api.controller;

import com.taskflow.taskflow_api.dto.CreateBoardRequest;
import com.taskflow.taskflow_api.dto.UpdateTaskRequest;
import com.taskflow.taskflow_api.entity.Task;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Task createTask(@PathVariable Long listId, @RequestBody CreateBoardRequest request) {
        return taskService.createTask(listId, request.getTitle());
    }
    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable Long listId, @PathVariable Long taskId,
                            @RequestBody UpdateTaskRequest request,
                            @AuthenticationPrincipal User currentUser) {
        return taskService.updateTask(taskId, request.getTitle(), request.getStatus(), currentUser.getId());
    }
 
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long listId, @PathVariable Long taskId,
                            @AuthenticationPrincipal User currentUser) {
        taskService.deleteTask(taskId, currentUser.getId());
    }
}
