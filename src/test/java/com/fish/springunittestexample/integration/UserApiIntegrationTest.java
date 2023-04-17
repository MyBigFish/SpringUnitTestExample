package com.fish.springunittestexample.integration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fish.springunittestexample.entity.Response;
import com.fish.springunittestexample.entity.User;
import com.fish.springunittestexample.mapper.UserMapper;
import com.fish.springunittestexample.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author dayuqichengbao
 * @version 创建时间 2023/4/16 11:29
 */
public class UserApiIntegrationTest extends AbstractIntegrationTest{


    @BeforeAll
    public static void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Resource
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    private static HttpHeaders headers;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private String usersURLWithPort() {
        return "http://localhost:" + port + "/api/getUsers";
    }

    private String saveURLWithPort() {
        return "http://localhost:" + port + "/api/saveUser";
    }

    private String getURLWithPort(int id) {
        return "http://localhost:" + port + "/api/users/" + id;
    }

    @Test
    @Sql(statements = "INSERT INTO `user`(`nickname`, `is_enable`, `create_time`, `update_time`, `is_deleted`) VALUES ('tom', 1, '2022-05-05 16:12:40', '2022-07-14 16:46:10',0)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM `user` WHERE id < 100000",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testOrdersList() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ParameterizedTypeReference<Response<List<User>>> reference = new ParameterizedTypeReference<Response<List<User>>>() {
        };
        ResponseEntity<Response<List<User>>> response = restTemplate.exchange(
                usersURLWithPort(), HttpMethod.GET,
                entity,
                reference);
        Response<List<User>> body = response.getBody();
        assert body != null;
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals(body.getData().size(), userService.getUsers().size());
        assertEquals(body.getData().size(), userMapper.selectList(null).size());
    }

    @Test
    @Sql(statements = "INSERT INTO `user`(id, `nickname`, `is_enable`, `create_time`, `update_time`, `is_deleted`) VALUES (100,'tom', 1, '2022-05-05 16:12:40', '2022-07-14 16:46:10',0)",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM `user` WHERE id = 100",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testGetUserById() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ParameterizedTypeReference<Response<User>> reference = new ParameterizedTypeReference<Response<User>>() {
        };
        ResponseEntity<Response<User>> response = restTemplate.exchange(
                getURLWithPort(100), HttpMethod.GET,
                entity,
                reference);
        Response<User> body = response.getBody();
        assert body != null;
        assertEquals(response.getStatusCodeValue(), 200);
        assertEquals("tom", userMapper.selectById(100).getNickname());

    }

    @Test
    @Sql(statements = "DELETE FROM user WHERE id='3'", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void testSaveUser() throws JsonProcessingException {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Module timeModule = new JavaTimeModule()
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter))
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(timeFormatter))
                .addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(timeFormatter));
        objectMapper.registerModule(timeModule);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        User user1 = new User(3L, "Tom", true, true);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(user1), headers);

        ParameterizedTypeReference<Response<User>> reference = new ParameterizedTypeReference<Response<User>>() {
        };

        ResponseEntity<Response<User>> response = restTemplate.exchange(
                saveURLWithPort(), HttpMethod.POST, entity, reference);

        User user = Objects.requireNonNull(response.getBody()).getData();
        assertEquals(user.getNickname(), "Tom");
    }

}
