package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.dto.BoardResponseDTO;
import com.taskflow.taskflow_api.dto.CreateBoardRequest;
import com.taskflow.taskflow_api.entity.Board;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.exception.BoardNotFoundException;
import com.taskflow.taskflow_api.exception.UnauthorizedAccessException;
import com.taskflow.taskflow_api.repository.BoardRepository;
import com.taskflow.taskflow_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public List<BoardResponseDTO> getBoardsForUser(Long userId) {
        return boardRepository.findByUserId(userId).stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDTO getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
        return new BoardResponseDTO(board);
    }

    public BoardResponseDTO createBoard(Long userId, CreateBoardRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Board board = new Board();
        board.setTitle(request.getTitle());
        board.setUser(user);
        Board saved = boardRepository.save(board);
        return new BoardResponseDTO(saved);
    }

    public BoardResponseDTO updateBoard(Long id, CreateBoardRequest request,Long currentUserId) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
        if(!board.getUser().getId().equals(currentUserId)){
            throw new UnauthorizedAccessException("You do not have permission to edit this board");
        }
         board.setTitle(request.getTitle());
        Board saved = boardRepository.save(board);
        return new BoardResponseDTO(saved);
    }

    public void deleteBoard(Long id,Long currentUserId) {
         Board board = boardRepository.findById(id)
          .orElseThrow(() -> new BoardNotFoundException(id));
            if (!board.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this board");
        }
 
        boardRepository.deleteById(id);
    }
}
