package com.taskflow.taskflow_api.dto;
public class LoginRequest {
    @Email @NotBlank private String email;
    @NotBlank private String password;
    // getters/setters
}