package com.override.security.service;


import com.override.security.model.Role;
import com.override.security.model.User;
import com.override.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private SessionRegistryImpl sessionRegistry;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUser(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElseThrow();
    }

    public boolean saveUser(User user) {
//        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        Optional<User> optionalUser = userRepository.findByName(user.getName());
        if (optionalUser.isPresent()) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Transactional
    public void updateUser(User updatedUser) {
        User newUser = userRepository.findById(updatedUser.getId()).get();
        newUser.setName(updatedUser.getName());
        newUser.setLastName(updatedUser.getLastName());
        newUser.setAge(updatedUser.getAge());
        newUser.setEmail(updatedUser.getEmail());
        newUser.setRoles(updatedUser.getRoles());
        userRepository.save(newUser);
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

    //Метод для удаления сессии любого пользователя
    public void expireUserSessions(User user) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof User) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getUsername().equals(user.getName())) {
                    for (SessionInformation information : sessionRegistry
                            .getAllSessions(userDetails, true)) {
                        information.expireNow();
                    }
                }
            }
        }
    }


}
