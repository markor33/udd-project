package com.example.udd.security.service.impl;

import com.example.udd.security.model.Role;
import com.example.udd.security.model.User;
import com.example.udd.security.repository.UserRepository;
import com.example.udd.security.service.interfaces.RoleService;
import com.example.udd.security.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    @Override
    public User findById(final Long id) {
        return userRepository.findById(id).orElseGet(null);
    }

    @Override
    public User findByEmail(final String email){
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(final User user, final String role) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        List<Role> roles = roleService.findByName(role);
        user.setRoles(roles);

        try{
            return this.userRepository.save(user);
        }
        catch (Exception e) {
            return null;
        }
    }
}
