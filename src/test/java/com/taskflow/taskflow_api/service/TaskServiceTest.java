package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.entity.Board;
import com.taskflow.taskflow_api.entity.Task;
import com.taskflow.taskflow_api.entity.TaskList;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.exception.TaskListNotFoundException;
import com.taskflow.taskflow_api.exception.TaskNotFoundException;
import com.taskflow.taskflow_api.exception.UnauthorizedAccessException;
import com.taskflow.taskflow_api.repository.TaskListRepository;
import com.taskflow.taskflow_api.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskListRepository taskListRepository;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository, taskListRepository);
    }

    // Small helper - builds a fake Task that belongs to ownerId,
    // through the chain Task -> TaskList -> Board -> User
    private Task buildTaskOwnedBy(Long ownerId) {
        User owner = new User();
        owner.setId(ownerId);

        Board board = new Board();
        board.setUser(owner);

        TaskList list = new TaskList();
        list.setBoard(board);

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Buy milk");
        task.setStatus("TODO");
        task.setTaskList(list);
        return task;
    }

    // ---------- createTask ----------

    @Test
    void createTask_shouldSaveTask_whenListExists() {
        // Arrange
        TaskList list = new TaskList();
        list.setId(1L);

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(list));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.createTask(1L, "Buy milk");

        // Assert
        assertEquals("Buy milk", result.getTitle());
        assertEquals("TODO", result.getStatus()); // new tasks always start as TODO
    }

    @Test
    void createTask_shouldThrowException_whenListDoesNotExist() {
        when(taskListRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TaskListNotFoundException.class, () -> {
            taskService.createTask(999L, "Buy milk");
        });
    }

    // ---------- updateTask ----------

    @Test
    void updateTask_shouldUpdateStatus_whenOwnedByCurrentUser() {
        // Arrange - task belongs to user 1L, and user 1L is the one asking
        Task task = buildTaskOwnedBy(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, null, "DONE", 1L);

        // Assert
        assertEquals("DONE", result.getStatus());
        assertEquals("Buy milk", result.getTitle()); // title untouched since we passed null
    }

    @Test
    void updateTask_shouldThrowException_whenTaskDoesNotExist() {
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(999L, "New title", null, 1L);
        });
    }

    // THIS is the ownership test for Task - proves Day 32/33 works here too
    @Test
    void updateTask_shouldThrowUnauthorized_whenNotOwnedByCurrentUser() {
        // Arrange - task belongs to user 1L, but user 2L is asking
        Task task = buildTaskOwnedBy(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // Act + Assert
        assertThrows(UnauthorizedAccessException.class, () -> {
            taskService.updateTask(1L, "Hacked title", null, 2L);
        });

        // Confirm it never actually saved the change
        verify(taskRepository, never()).save(any());
    }

    // ---------- deleteTask ----------

    @Test
    void deleteTask_shouldThrowUnauthorized_whenNotOwnedByCurrentUser() {
        Task task = buildTaskOwnedBy(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        assertThrows(UnauthorizedAccessException.class, () -> {
            taskService.deleteTask(1L, 2L);
        });

        verify(taskRepository, never()).deleteById(any());
    }
}
