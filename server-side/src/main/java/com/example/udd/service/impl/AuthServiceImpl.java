package com.example.udd.service.impl;

import com.example.udd.dto.LoginDTO;
import com.example.udd.security.TokenUtils;
import com.example.udd.security.model.User;
import com.example.udd.security.repository.UserRepository;
import com.example.udd.security.service.interfaces.RoleService;
import com.example.udd.security.service.interfaces.UserService;
import com.example.udd.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.example.udd.security.model.User.toUser;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Override
    public ResponseEntity<LoginDTO> login(final String username, final String password) {
        Authentication authentication = getAuthentication(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();

        String jwt = tokenUtils.generateToken(user);
        int expiresIn = tokenUtils.getExpiredIn();

        return ResponseEntity.ok().body(new LoginDTO(jwt, expiresIn));
    }

    @Override
    public void register(
            final String username,
            final String email,
            final String password,
            final String role
    ) {
        var user = toUser(username, email, password);
        userService.save(user, role);
    }

    private Authentication getAuthentication(final String email, final String password) {
        Authentication authentication;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (Exception ex){
            throw ex;
        }
        return authentication;
    }

}
