package com.fish.springunittestexample.service;

import com.fish.springunittestexample.entity.User;
import com.fish.springunittestexample.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author shulongliu
 * @version 创建时间 2023/4/16 10:56
 */
@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Test
    public void testGetUserList() {
        User user1 = new User(1L, "Tom", true, true);
        User user2 = new User(2L, "Jack", true, true);
        when(userMapper.selectList(null)).thenReturn(Arrays.asList(user1, user2));
        assertEquals(userService.getUsers().size(), 2);
        assertEquals(userService.getUsers().get(0).getNickname(), "Tom");
        assertEquals(userService.getUsers().get(1).getNickname(), "Jack");
        assertEquals(userService.getUsers().get(0).getId(), 1L);
        assertEquals(userService.getUsers().get(1).getId(), 2L);
    }

    @Test
    public void testGetUserById() {
        User user1 = new User(1L, "Tom", true, true);
        when(userMapper.selectById(1L)).thenReturn(user1);
        assertEquals(userService.getUserById(1L).getNickname(), "Tom");
        assertEquals(userService.getUserById(1L).getId(), 1L);
    }
    @Test
    public void testGetInvalidUserById() {
        Long userId = 17L;
        when(userMapper.selectById(userId)).thenReturn(null);
        Exception exception = assertThrows(RuntimeException.class, () -> userService.getUserById(userId));
        assertTrue(exception.getMessage().contains("user not exist"));
    }

    @Test
    public void testAddUser() {
        User user = new User(null, "john", true, true);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        when(userMapper.insert(userArgumentCaptor.capture())).thenReturn(10);

        assertDoesNotThrow(() -> userService.addUser(user));

        assertNull(userArgumentCaptor.getValue().getId());
        assertEquals("john", userArgumentCaptor.getValue().getNickname());

        verify(userMapper, times(1)).insert(any(User.class));

    }
}
