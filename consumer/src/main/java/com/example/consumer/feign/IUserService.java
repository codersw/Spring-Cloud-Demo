package com.example.consumer.feign;

import com.example.consumer.feign.fallback.UserServiceFallback;
import com.example.consumer.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "server", contextId = "userService", fallback = UserServiceFallback.class)
public interface IUserService {

    @GetMapping("/user")
    User get();
}
