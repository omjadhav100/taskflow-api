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

    private BoardService boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, userRepository);
    }

    @Test
    void getBoard_shouldReturnBoard_whenBoardExists() {
        Board board = new Board();
        board.setId(1L);
        board.setTitle("My Board");

        when(boardRepository.findById(1L)).thenReturn(Optional.of(board));

        BoardResponseDTO result = boardService.getBoard(1L);

        assertEquals(1L, result.getId());
        assertEquals("My Board", result.getTitle());
    }

    @Test
    void getBoard_shouldThrowException_whenBoardDoesNotExist() {
        when(boardRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BoardNotFoundException.class, () -> {
            boardService.getBoard(999L);
        });
    }

    @Test
    void createBoard_shouldSaveBoardWithCorrectUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        CreateBoardRequest request = new CreateBoardRequest("New Board");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(boardRepository.save(any(Board.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BoardResponseDTO result = boardService.createBoard(1L, request);

        assertEquals("New Board", result.getTitle());
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void createBoard_shouldThrowException_whenUserDoesNotExist() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        CreateBoardRequest request = new CreateBoardRequest("Board");

        assertThrows(RuntimeException.class, () -> {
            boardService.createBoard(999L, request);
        });

        verify(boardRepository, never()).save(any());
    }

    @Test
    void deleteBoard_shouldThrowException_whenBoardDoesNotExist() {
        when(boardRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BoardNotFoundException.class, () -> {
            boardService.deleteBoard(999L, 1L);
        });

        verify(boardRepository, never()).deleteById(any());
    }
}
