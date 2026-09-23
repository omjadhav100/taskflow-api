package com.taskflow.taskflow_api.controller;

import com.taskflow.taskflow_api.dto.CreateListRequest;
import com.taskflow.taskflow_api.entity.TaskList;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.service.TaskListService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards/{boardId}/lists")
public class TaskListController {

   private final TaskListService taskListService;
 
    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }
 
    @GetMapping
    public List<TaskList> getLists(@PathVariable Long boardId) {
        return taskListService.getListsForBoard(boardId);
    }
 
    @PostMapping
    public TaskList createList(@PathVariable Long boardId, @Valid @RequestBody CreateListRequest request) {
        return taskListService.createList(boardId, request.getName());
    }
 
    @PutMapping("/{listId}")
    public TaskList updateList(@PathVariable Long boardId, @PathVariable Long listId,
                                @Valid @RequestBody CreateListRequest request,
                                @AuthenticationPrincipal User currentUser) {
        return taskListService.updateList(listId, request.getName(), currentUser.getId());
    }
 
    @DeleteMapping("/{listId}")
    public void deleteList(@PathVariable Long boardId, @PathVariable Long listId,
                            @AuthenticationPrincipal User currentUser) {
        taskListService.deleteList(listId, currentUser.getId());
    }
}