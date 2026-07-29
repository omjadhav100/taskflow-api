package com.taskflow.taskflow_api.dto;

public class CreateBoardRequest {
 @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be under 100 characters")
    private String title;

    public CreateBoardRequest() {
    }

    public CreateBoardRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
