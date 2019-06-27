package com.willpower.ssm.consumer.controller;

import com.willpower.ssm.consumer.service.UserService;
import com.willpower.ssm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Powersoft on 2019/6/27.
 */
@RestController
public class UserController {

    @Autowired
    UserService UserService;


    /**
     * 调用方RestController，这里接收浏览器请求，使用@PathVariable
     * @param id
     * @return
     */
    @RequestMapping("/user/{id}")
    public User getUserById(@PathVariable("id") int id) {
        return UserService.getUserById(id);
    }
}
