package com.zihan.small_inventory;

import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthTests {
    @Autowired
    private TestRestTemplate restTemplate;

    // Register successfully
    @Test
    void registerTest() {
        Map<String, String> registerRequest = Map.of(
                "shopId", "testshop1",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertTrue(registerResponse.isSuccess());
    }

    // Register with invalid ID
    @Test
    void registerTestId(){
        Map<String, String> registerRequest = Map.of(
                "shopId", "test",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );

        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertFalse(registerResponse.isSuccess());
        assertTrue(registerResponse.validateCode(401));
    }

    // Register with invalid password
    @Test
    void registerTestPassword(){
        Map<String, String> registerRequest = Map.of(
                "shopId", "testshop2",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "passwor"
        );

        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertFalse(registerResponse.isSuccess());
        assertTrue(registerResponse.validateCode(402));
    }

    // Register with duplicate ID
    @Test
    void registerTestIdUsed(){
        Map<String, String> registerRequest = Map.of(
                "shopId", "testshop2",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertTrue(registerResponse.isSuccess());

        Map<String, String> registerRequest2 = Map.of(
                "shopId", "testshop2",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse2 = restTemplate.postForObject("/auth/register", registerRequest2, ResponseUtil.class);
        assertFalse(registerResponse2.isSuccess());
        assertTrue(registerResponse2.validateCode(403));
    }

    // Login successfully
    @Test
    void loginTest() {
        Map<String, String> registerRequest = Map.of(
                "shopId", "testshop4",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertTrue(registerResponse.isSuccess());

        // call the endpoint with type reference
        ResponseEntity<ResponseUtil<Map<String, Object>>> response = restTemplate.exchange(
                "/auth/login?shopId=testshop4&password=password123",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<ResponseUtil<Map<String, Object>>>() {}
        );

        ResponseUtil<Map<String, Object>> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());

        Map<String, Object> data = body.getData();
        assertNotNull(data.get("token"));
        assertNotNull(data.get("shopId"));
    }

    // Login with invalid ID
    @Test
    void loginTestId(){

        ResponseUtil<?> loginResponse = restTemplate.postForObject("/auth/login?shopId=mys00000003&password=mypassword", null, ResponseUtil.class);

        assertFalse(loginResponse.isSuccess());
        assertTrue(loginResponse.validateCode(404));
    }

    // Login with invalid password
    @Test
    void loginTestPassword(){

        Map<String, String> registerRequest = Map.of(
                "shopId", "testshop3",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertTrue(registerResponse.isSuccess());

        ResponseUtil<?> loginResponse = restTemplate.postForObject("/auth/login?shopId=testshop3&password=password", null, ResponseUtil.class);

        assertFalse(loginResponse.isSuccess());
        assertTrue(loginResponse.validateCode(405));
    }

    @AfterAll
    static void cleanup() {
    }
}
