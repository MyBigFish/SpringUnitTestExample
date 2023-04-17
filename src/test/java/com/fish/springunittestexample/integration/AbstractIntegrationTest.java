package com.fish.springunittestexample.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

/**
 * @author shulongliu
 * @version 创建时间 2023/4/17 09:56
 */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIntegrationTest {
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:5.7")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("123456")
            .withReuse(true)
            .withInitScript("user.sql");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
    }

    @BeforeAll
    public static void start() {
        mySQLContainer.start();
    }
}
