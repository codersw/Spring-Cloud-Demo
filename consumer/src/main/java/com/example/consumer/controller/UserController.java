package com.example.consumer.controller;

import com.example.consumer.feign.IUserService;
import com.example.consumer.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @GetMapping
    public User get() {
        return userService.get();
    }
}
