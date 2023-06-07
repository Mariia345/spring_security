package com.softserve.itacademy.controller;

import com.softserve.itacademy.model.User;
import com.softserve.itacademy.security.UserDetailsImpl;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public LoginController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @GetMapping("/login-form")
    public String loginPage() {
        return "login-page";
    }

    @GetMapping("/registration")
    public String registrationPage(@Validated @ModelAttribute("user") User user) {
        return "create-user";
    }

    @GetMapping("/redirection")
    public String redirection(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            return "redirect:/home";
        }else{
        //if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))){
            return "redirect:/todos/all/users/"+(((UserDetailsImpl)authentication.getPrincipal()).getUser().getId());
        }
    }

    @PostMapping("/registration")
    public String registration(@Validated @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "create-user";
        }
        user.setPassword(user.getPassword());
        user.setRole(roleService.readById(2));
        User newUser = userService.create(user);
        return "redirect:/login-form" + newUser.getId();
    }
}

