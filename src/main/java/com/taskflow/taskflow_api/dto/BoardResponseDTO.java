package com.taskflow.taskflow_api.dto;

import com.taskflow.taskflow_api.entity.Board;

public class BoardResponseDTO {

    private Long id;
    private String title;
    private int listCount;

    public BoardResponseDTO(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.listCount = board.getLists().size();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getListCount() {
        return listCount;
    }
}
