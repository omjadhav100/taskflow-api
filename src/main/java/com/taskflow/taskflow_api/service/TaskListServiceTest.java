package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.entity.Board;
import com.taskflow.taskflow_api.entity.TaskList;
import com.taskflow.taskflow_api.exception.BoardNotFoundException;
import com.taskflow.taskflow_api.repository.BoardRepository;
import com.taskflow.taskflow_api.repository.TaskListRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskListServiceTest {

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private BoardRepository boardRepository;

    private TaskListService taskListService;

    @BeforeEach
    void setUp() {
        taskListService = new TaskListService(taskListRepository, boardRepository);
    }

    // ---------- createList tests ----------

    @Test
    void createList_shouldSaveList_whenBoardExists() {
        // Arrange
        Board board = new Board();
        board.setId(1L);
        board.setTitle("My Board");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        // "just return whatever TaskList was passed into save()" -
        // simulates the database handing back the saved entity
        when(taskListRepository.save(any(TaskList.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TaskList result = taskListService.createList(1L, "To Do");

        // Assert
        assertEquals("To Do", result.getName());
        assertEquals(board, result.getBoard());
        verify(taskListRepository, times(1)).save(any(TaskList.class));
    }

    @Test
    void createList_shouldThrowException_whenBoardDoesNotExist() {
        // Arrange
        when(boardRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BoardNotFoundException.class, () -> {
            taskListService.createList(999L, "To Do");
        });

        // Confirm it never tried to save a list for a board that doesn't exist
        verify(taskListRepository, never()).save(any());
    }

    // ---------- getListsForBoard test ----------

    @Test
    void getListsForBoard_shouldReturnAllListsForThatBoard() {
        // Arrange
        Board board = new Board();
        board.setId(1L);

        TaskList list1 = new TaskList();
        list1.setId(1L);
        list1.setName("To Do");
        list1.setBoard(board);

        TaskList list2 = new TaskList();
        list2.setId(2L);
        list2.setName("Done");
        list2.setBoard(board);

        when(taskListRepository.findByBoardId(1L)).thenReturn(Arrays.asList(list1, list2));

        // Act
        List<TaskList> result = taskListService.getListsForBoard(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals("To Do", result.get(0).getName());
        assertEquals("Done", result.get(1).getName());
    }
}