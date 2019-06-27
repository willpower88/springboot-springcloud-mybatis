package com.willpower.ssm.consumer.controller;

import com.willpower.ssm.entity.User;
import javax.websocket.server.PathParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Powersoft on 2019/6/26.
 */
@RestController
public class UserController {

    /**
     * springcloud provider controller方法的参数需要@PathParam，由consumer传过来
     * @param id
     * @return 用户信息
     */
    @RequestMapping("/user")
    public User getUserById(@PathParam("id") int id) {
        User user = new User();
        user.setId(id);
        user.setName("张三");
        user.setAge(29);

        return user;
    }

}
