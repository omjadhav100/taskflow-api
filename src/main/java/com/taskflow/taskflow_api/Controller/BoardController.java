package com.taskflow.taskflow_api.controller;

import com.taskflow.taskflow_api.dto.BoardResponseDTO;
import com.taskflow.taskflow_api.dto.CreateBoardRequest;
import com.taskflow.taskflow_api.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    // Constructor injection - this was completely missing before.
    // Without this, the controller had no way to talk to the database at all,
    // which is why every method just returned null.
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public List<BoardResponseDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    public BoardResponseDTO getBoard(@PathVariable Long id) {
        return boardService.getBoard(id);
    }

    @PostMapping
    public BoardResponseDTO createBoard(@RequestBody CreateBoardRequest request) {
        return boardService.createBoard(request);
    }

    @PutMapping("/{id}")
    public BoardResponseDTO updateBoard(@PathVariable Long id,
                                         @RequestBody CreateBoardRequest request) {
        return boardService.updateBoard(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
    }
}
