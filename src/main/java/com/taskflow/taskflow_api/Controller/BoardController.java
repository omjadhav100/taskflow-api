package com.taskflow.taskflow_api.controller;

import com.taskflow.taskflow_api.dto.BoardResponseDTO;
import com.taskflow.taskflow_api.dto.CreateBoardRequest;
import com.taskflow.taskflow_api.entity.User;
import com.taskflow.taskflow_api.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<BoardResponseDTO> getMyBoards(@AuthenticationPrincipal User currentUser) {
        return boardService.getBoardsForUser(currentUser.getId());
    }

    @GetMapping("/{id}")
    public BoardResponseDTO getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PostMapping
    public BoardResponseDTO createBoard(@Valid @RequestBody CreateBoardRequest request,
                                         @AuthenticationPrincipal User currentUser) {
        return boardService.createBoard(currentUser.getId(), request);
    }

    @PutMapping("/{id}")
    public BoardResponseDTO updateBoard(@PathVariable Long id,
                                         @Valid @RequestBody CreateBoardRequest request,
                                         @AuthenticationPrincipal User currentUser) {
        return boardService.updateBoard(id, request, currentUser.getId());
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        boardService.deleteBoard(id, currentUser.getId());
    }
}