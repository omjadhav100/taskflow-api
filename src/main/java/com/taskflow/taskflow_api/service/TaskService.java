package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.entity.Task;
import com.taskflow.taskflow_api.entity.TaskList;
import com.taskflow.taskflow_api.exception.TaskListNotFoundException;
import com.taskflow.taskflow_api.repository.TaskListRepository;
import com.taskflow.taskflow_api.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    public Task createTask(Long listId, String title) {
        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new TaskListNotFoundException(listId));

        Task task = new Task();
        task.setTitle(title);
        task.setStatus("TODO");
        task.setTaskList(taskList);
        return taskRepository.save(task);
    }

    public List<Task> getTasksForList(Long listId) {
        return taskRepository.findByTaskListId(listId);
    }
}
