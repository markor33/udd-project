package com.example.udd.controller;

import com.example.udd.dto.LoginDTO;
import com.example.udd.request.LoginRequest;
import com.example.udd.request.RegisterRequest;
import com.example.udd.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path="/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LoginDTO> login(@RequestBody final LoginRequest loginRequest) {

        return authService.login(
                loginRequest.username(),
                loginRequest.password()
        );
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity signupPassenger(@RequestBody final RegisterRequest registrationRequest) {
        authService.register(
                registrationRequest.username(),
                registrationRequest.email(),
                registrationRequest.password(),
                registrationRequest.role()
        );
        return ResponseEntity.ok().build();
    }

}
