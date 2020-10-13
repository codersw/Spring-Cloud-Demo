package com.example.consumer.feign.fallback;

import com.example.consumer.feign.IUserService;
import com.example.consumer.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserServiceFallback implements IUserService {

    @Override
    public User get() {
        log.error("get方法出错金进入熔断");
        return User.builder().build();
    }
}
