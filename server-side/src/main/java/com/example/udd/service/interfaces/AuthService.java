package com.example.udd.service.interfaces;

import com.example.udd.dto.LoginDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginDTO> login(final String username, final String password);
    void register(final String username, final String email, final String password, final String role);
}
