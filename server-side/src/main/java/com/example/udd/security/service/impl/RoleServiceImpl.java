package com.example.udd.security.service.impl;

import com.example.udd.security.model.Role;
import com.example.udd.security.repository.RoleRepository;
import com.example.udd.security.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findById(final Long id) {
        return this.roleRepository.getOne(id);
    }

    @Override
    public List<Role> findByName(final String name) {
        return this.roleRepository.findByName(name);
    }

}
