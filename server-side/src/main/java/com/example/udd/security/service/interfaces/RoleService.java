package com.example.udd.security.service.interfaces;

import com.example.udd.security.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(final Long id);
    List<Role> findByName(final String name);
}
