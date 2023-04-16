package com.fish.springunittestexample.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fish.springunittestexample.entity.User;
import com.fish.springunittestexample.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户服务
 *
 * @author shulongliu
 * @version  2023/04/16
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 添加用户
     */
    public int addUser(User user) {
        return userMapper.insert(user);
    }

    /**
     * 根据id查询用户
     *
     * @param userId 用户id
     * @return {@link User}
     */
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("user not exist");
        }
        return user;
    }

    /**
     * 查询用户
     *
     * @return {@link List}<{@link User}>
     */
    public List<User> getUsers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("id", 0);
        return userMapper.selectList(null);
    }

}
