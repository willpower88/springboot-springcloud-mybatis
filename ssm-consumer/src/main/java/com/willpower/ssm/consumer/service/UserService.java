package com.willpower.ssm.consumer.service;

import com.willpower.ssm.entity.User;
import javax.websocket.server.PathParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Powersoft on 2019/6/27.
 */
@Service
@FeignClient("ssm-service")
public interface UserService {

    /**
     * 微服务调用接口,注意这里的参数据用@RequestParam
     * @param id
     * @return
     */
    @RequestMapping("/user")
    public User getUserById(@RequestParam("id") int id);

}
