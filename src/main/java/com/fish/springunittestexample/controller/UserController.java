package com.fish.springunittestexample.controller;

import com.fish.springunittestexample.entity.Response;
import com.fish.springunittestexample.entity.User;
import com.fish.springunittestexample.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制器
 *
 * @author dayuqichengbao
 * @date 2023/04/16
 */
@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping(path = "/saveUser")
    public Response<User> saveUser(@RequestBody User user) {
        userService.addUser(user);
        return Response.ok(user);
    }

    @GetMapping(path = "/users/{id}")
    public Response<User> getUser(@PathVariable Long id) {
        return Response.ok(userService.getUserById(id));
    }

    @GetMapping(path = "/getUsers")
    public Response<List<User>> getAllUsers() {
        return Response.ok(userService.getUsers());
    }

}
