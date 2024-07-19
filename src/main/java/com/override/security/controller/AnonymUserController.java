package com.override.security.controller;


import com.override.security.model.User;
import com.override.security.repository.RoleRepository;
import com.override.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class AnonymUserController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/")
    public String getHomePage() {
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }

//    @GetMapping("/register")
//    public String registerPage(Model model) {
//        model.addAttribute("user", new User());
//        return "registration-page";
//    }
//
//    @PostMapping()
//    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "registration-page";
//        }
//        user.setRoles(Collections.singleton(roleRepository.findByRole("ROLE_USER")));
//        if (!userDetailsServiceImpl.saveUser(user)) {
//            model.addAttribute("NameError", "Пользователь с таким именем уже существует");
//            return "registration-page";
//        }
//        return "redirect:/";
//    }
}
