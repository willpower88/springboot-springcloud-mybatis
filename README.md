# springboot-sprintcloud-mybatis


<!-- vim-markdown-toc GFM -->

* [1. 新建工程](#1-新建工程)
* [2. 实现服务发现服务](#2-实现服务发现服务)
* [3. 实现微服务（Provider）](#3-实现微服务provider)
* [4. 实现微服务调用方（Consumer）](#4-实现微服务调用方consumer)
* [5. feign.FeignException: status 404 reading](#5-feignfeignexception-status-404-reading)
* [6. mybatis部分后续集成](#6-mybatis部分后续集成)

<!-- vim-markdown-toc -->

## 1. 新建工程
    1. 新建spring工程 springboot-springcloud
## 2. 实现服务发现服务
    1. 新建module工程 eureka-server，Dependencies -> Spring Cloud Discovery -> Eureka Server
    1. 启动类添加@EnableEurekaServer
    ```
    @SpringBootApplication
    @EnableEurekaServer
    public class EurekaServerApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(EurekaServerApplication.class, args);
    	}

    }
    ```
    1. 配置文件application.yml
    ```
    #eureka
    server:
      port: 8761 # 8761是eureka server的默认端口
    eureka:
      server:
        enable-self-preservation: false #防止由于Eureka的机制导致Client被错误显示在线 仅在开发环境使用
      client:
        service-url:
          defaultZone: http://localhost:8761/eureka/ #这便是此eureka server的应用注册地址
        register-with-eureka: false #不显示对server应用的注册
    spring:
      application:
        name: eureka-server
    ```
    1. 启动服务发现
        + 访问 http://localhost:8761/

## 3. 实现微服务（Provider）
    1. 新建module工程 ssm-service，
        + Dependencies -> Spring Cloud Discovery -> Eureka Discovery Client
        + Dependencies -> Web -> Spring Web Starter

    1. 启动类添加@EnableDiscoveryClient
    ```
    @SpringBootApplication
    @EnableDiscoveryClient
    public class SsmServiceApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SsmServiceApplication.class, args);
    	}

    }
    ```
    1. 配置文件application.yml
    ```
    server:
      port: 8081

    eureka:
      client:
        service-url:
          defaultZone: http://localhost:8761/eureka/ #注册到刚才那台Eureka Server地址
        register-with-eureka: true

    spring:
      application:
        name: ssm-service
    ```
    1. 新建Entity User 代码略
    1. 新建UserController
    ```
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
    ```
    1. 启动微服务
        + 打开http://localhost:8761/
        + 查看Instances currently registered with Eureka 部分,查看是否注册成功
## 4. 实现微服务调用方（Consumer）
    1.新建module工程 ssm-service，
        + Dependencies -> Spring Cloud Discovery -> Eureka Discovery Client
        + Dependencies -> Spring Cloud Routing -> OpenFeign
        + Dependencies -> Web -> Spring Web Starter

    1.启动类添加@EnableDiscoveryClient和@EnableFeignClients
    ```
    @SpringBootApplication
    @EnableDiscoveryClient
    @EnableFeignClients
    public class SsmConsumerApplication {

    	public static void main(String[] args) {
    		SpringApplication.run(SsmConsumerApplication.class, args);
    	}

    }
    ```
    1. 配置文件application.yml
    ```
    server:
      port: 8082
    spring:
      application:
        name: ssm-consumer
    eureka:
      client:
        serviceUrl:
          defaultZone: http://localhost:8761/eureka
    ```
    1. 新建调用方UserService
    ```
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
    ```
    1. 新建调用方UserController
    ```
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

    ```
    1. 启动微服务调用方
        + 打开http://localhost:8761/
        + 查看Instances currently registered with Eureka 部分,查看是否注册成功
        + http://localhost:8082/user/1

## 5. feign.FeignException: status 404 reading
    + 出现404的原因：
        1. 微服务部分Controller的参数错写为@PathVariable，正确的是@PathParam
        2. 微服务调用方的Service部分的参数错写为@PathParam，正确的是@RequestParam

## 6. mybatis部分后续集成
