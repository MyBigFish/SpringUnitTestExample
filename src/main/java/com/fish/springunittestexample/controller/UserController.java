package com.fish.springunittestexample.controller;

import com.fish.springunittestexample.entity.Response;
import com.fish.springunittestexample.entity.User;
import com.fish.springunittestexample.service.UserService;
import com.fish.springunittestexample.util.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户控制器
 *
 * @author shulongliu
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
        return ResponseUtil.successWithData(user);
    }

    @GetMapping(path = "/users/{id}")
    public Response<User> getUser(@PathVariable Long id) {
        return ResponseUtil.successWithData(userService.getUserById(id));
    }

    @GetMapping(path = "/getUsers")
    public Response<List<User>> getAllUsers() {
        return ResponseUtil.successWithData(userService.getUsers());
    }

}
