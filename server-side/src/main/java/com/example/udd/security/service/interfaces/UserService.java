package com.example.udd.security.service.interfaces;

import com.example.udd.security.model.User;

import java.util.List;

public interface UserService {
    User findById(final Long id);
    User findByEmail(final String email);
    User findByUsername(final String username);
    List<User> findAll ();
    User save(final User user, final String role);
}
