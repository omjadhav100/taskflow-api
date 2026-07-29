package com.taskflow.taskflow_api.dto;

public class CreateListRequest {
      @NotBlank(message = "List name is required")
    private String name;

    public CreateListRequest() {
    }

    public CreateListRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
