package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.dto.BoardResponseDTO;
import com.taskflow.taskflow_api.dto.CreateBoardRequest;
import com.taskflow.taskflow_api.entity.Board;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.exception.BoardNotFoundException;
import com.taskflow.taskflow_api.repository.BoardRepository;
import com.taskflow.taskflow_api.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    // NOT using @InjectMocks here on purpose - some BoardService versions have
    // 1 constructor arg, some have 2 (userRepository), depending on how far
    // you've built it. Creating it manually in setUp() avoids confusion either way.
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        // Runs before EVERY test method below - keeps each test starting fresh.
        boardService = new BoardService(boardRepository, userRepository);
    }

    // ---------- getBoard tests ----------

    @Test
    void getBoard_shouldReturnBoard_whenBoardExists() {
        // Arrange
        Board board = new Board();
        board.setId(1L);
        board.setTitle("My Board");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        // Act
        BoardResponseDTO result = boardService.getBoard(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("My Board", result.getTitle());
    }

    @Test
    void getBoard_shouldThrowException_whenBoardDoesNotExist() {
        // Arrange
        when(boardRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert combined - assertThrows runs the lambda and checks
        // that the exception type matches
        assertThrows(BoardNotFoundException.class, () -> {
            boardService.getBoard(999L);
        });
    }

    // ---------- createBoard tests ----------

    @Test
    void createBoard_shouldSaveBoardWithCorrectUser() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        CreateBoardRequest request = new CreateBoardRequest("New Board");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // thenAnswer here means "just return whatever Board object was passed
        // into save(), as if the database saved it and gave it back to us"
        when(boardRepository.save(any(Board.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        BoardResponseDTO result = boardService.createBoard(1L, request);

        // Assert
        assertEquals("New Board", result.getTitle());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void createBoard_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        CreateBoardRequest request = new CreateBoardRequest("Board");

        // Act + Assert
        assertThrows(RuntimeException.class, () -> {
            boardService.createBoard(999L, request);
        });

        // Extra check: confirm it did NOT try to save a board anyway
        // when the user lookup failed
        verify(boardRepository, never()).save(any());
    }

    // ---------- deleteBoard test ----------

    @Test
    void deleteBoard_shouldThrowException_whenBoardDoesNotExist() {
        // Arrange
        when(boardRepository.existsById(999L)).thenReturn(false);

        // Act + Assert
        assertThrows(BoardNotFoundException.class, () -> {
            boardService.deleteBoard(999L);
        });

        // Confirm deleteById was never called since the board didn't exist
        verify(boardRepository, never()).deleteById(any());
    }
}