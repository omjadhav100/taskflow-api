package com.taskflow.taskflow_api.dto;
public class RegisterRequest {

    @NotBlank private String name;
    @Email @NotBlank private String email;
    @NotBlank @Size(min = 8, message = "Password must be at least 8 characters") private String password;
    // getters/setters
}