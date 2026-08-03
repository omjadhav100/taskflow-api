package com.taskflow.taskflow_api.controller;
@RestContoller
@RequestMapping("/api/auth")
public class AuthController{
    private final AuthService authService;
    public AuthController(AuthService authService) {this.authService=authService;}

    @PostMapping("/register")
    public AuthResponse register(@valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }
    @PostMapping("/login")
    public AuthResponse login(@valid @RequestBody LoginRequest request){
        return authService.login(request);
    }
}
