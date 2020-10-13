package com.example.server.controller;

import com.example.server.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public User get() {
        return User.builder()
                .userId(1)
                .userName("邵文")
                .build();
    }
}
