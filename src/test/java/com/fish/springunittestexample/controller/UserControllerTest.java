package com.fish.springunittestexample.controller;

import com.baomidou.mybatisplus.test.autoconfigure.AutoConfigureMybatisPlus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fish.springunittestexample.entity.User;
import com.fish.springunittestexample.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 用户控制器测试
 * @author dayuqichengbao
 * @version 创建时间 2023/4/16 09:45
 * @date 2023/04/16
 */
@SpringJUnitConfig
@WebMvcTest(UserController.class)
@AutoConfigureMybatisPlus
public class UserControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("root")
            .withReuse(true);
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @BeforeEach
    public void setUp() {
        mySQLContainer.start();
        createObjectMapper();
    }

    private void createObjectMapper() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(timeFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(timeFormatter));
        objectMapper.registerModule(timeModule);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    public void testGetUsers() throws Exception {
        User user = User.builder().nickname("test").isEnable(true).build();
        when(userService.getUsers()).thenReturn(Collections.singletonList(user));
        mockMvc.perform(get("/api/getUsers"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setNickname("Tom");
        user.setIsDeleted(false);
        user.setIsEnable(true);
        user.setUpdateTime(LocalDateTime.now());
        user.setCreateTime(LocalDateTime.now());
        when(userService.getUserById(1L)).thenReturn(user);
        mockMvc.perform(get("/api/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.nickname", is("Tom")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$").isNotEmpty());
    }


    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setNickname("Tom");
        user.setIsDeleted(false);
        user.setIsEnable(true);
        user.setUpdateTime(LocalDateTime.now());
        user.setCreateTime(LocalDateTime.now());
        user.setId(1L);
        when(userService.addUser(user)).thenReturn(1);

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(
                        post("/api/saveUser")
                                .content(userJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$").isNotEmpty());
    }

}
