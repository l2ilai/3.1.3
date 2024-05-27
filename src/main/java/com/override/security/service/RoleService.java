package com.override.security.service;

import com.override.security.model.Role;
import com.override.security.model.User;
import com.override.security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }






}
