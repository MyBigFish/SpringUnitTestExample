package com.fish.springunittestexample.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author shulongliu
 * @version 创建时间 2023/4/17 16:55
 */
public class ContainerTest extends AbstractIntegrationTest {

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

    private String usersURLWithPort() {
        return "http://localhost:" + port + "/api/getUsers";
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
}
