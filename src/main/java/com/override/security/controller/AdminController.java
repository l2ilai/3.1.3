package com.override.security.controller;

import com.override.security.model.Role;
import com.override.security.model.User;
import com.override.security.service.RoleService;
import com.override.security.service.UserDetailsServiceImpl;
import com.override.security.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RoleService roleService;

    @GetMapping(path = {"/allUsers"})
    public List<User> getAdminPage() {
        return userDetailsService.findAllUsers();
    }

    @GetMapping(path = {"/new"})
    public List<Role> newUser() {
        return roleService.findAllRoles();
    }

    @PostMapping("/createUser")
    public String createUser(User user) {
        userDetailsService.saveUser(user);
        return "{\"status\":\"success\"}";
    }

    @PostMapping(path = {"/edit/{id}"})
    public String editUser(@PathVariable("id") Long id, User user) {
        user.setId(id);
        userDetailsService.saveUser(user);
        return "{\"status\":\"success\"}";
    }

    @RequestMapping(value = "/get/{id}")
    public User getUser(@PathVariable(name = "id") Long id) {
        return userDetailsService.findUser(id);
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userDetailsService.deleteUser(id);
        return "{\"status\":\"success\"}";
    }
}
