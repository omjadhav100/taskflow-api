package com.taskflow.taskflow_api.service;

import com.taskflow.taskflow_api.dto.BoardResponseDTO;
import com.taskflow.taskflow_api.dto.CreateBoardRequest;
import com.taskflow.taskflow_api.entity.Board;
import com.taskflow.taskflow_api.exception.BoardNotFoundException;
import com.taskflow.taskflow_api.repository.BoardRepository;

import main.java.com.taskflow.taskflow_api.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
     private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    public List<BoardResponseDTO> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDTO::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDTO getBoard(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
        return new BoardResponseDTO(board);
    }

    public BoardResponseDTO createBoard(CreateBoardRequest request) {
        Board board = new Board();
        board.setTitle(request.getTitle());
         board.setUser(user);
        Board saved = boardRepository.save(board);
        return new BoardResponseDTO(saved);
    }

    public BoardResponseDTO updateBoard(Long id, CreateBoardRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new BoardNotFoundException(id));
        board.setTitle(request.getTitle());
        Board saved = boardRepository.save(board);
        return new BoardResponseDTO(saved);
    }

    public void deleteBoard(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new BoardNotFoundException(id);
        }
        boardRepository.deleteById(id);
    }
}
