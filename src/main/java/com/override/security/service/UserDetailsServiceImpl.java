package com.override.security.service;


import com.override.security.model.Role;
import com.override.security.model.User;
import com.override.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElseThrow();
    }

    @Transactional
    public void saveUser(User user) {
//        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(User updateUser) {
        User userToBeUpdated = findUser(updateUser.getId());
        userToBeUpdated.setName(updateUser.getName());
        userToBeUpdated.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        userToBeUpdated.setRoles(updateUser.getRoles());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByName(name);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return user.get();
    }

    public boolean isNotRoleAdmin(Authentication authentication, User user) {
        Role roleAdmin = new Role(2L, "ROLE_ADMIN");
        User authenticatedUser = (User) authentication.getPrincipal();
        if (user.getName().equals(authenticatedUser.getName())) {
            Set<Role> userRoles = user.getRoles();
            return !userRoles.contains(roleAdmin);
        }
        return false;

    }

    public User getAuthenticatedUser(Authentication authentication) {
        User authenticatedUser =  (User) authentication.getPrincipal();
        return findUser(authenticatedUser.getId());
    }
}
