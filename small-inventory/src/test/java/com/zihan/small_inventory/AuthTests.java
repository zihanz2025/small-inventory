package com.zihan.small_inventory;

import com.zihan.small_inventory.constants.ResponseCode;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthTests {
    @Autowired
    private TestRestTemplate restTemplate;

    // Register successfully
    @BeforeAll
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
        assertTrue(registerResponse.validateCode(ResponseCode.REGISTRATION_FAILED));
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
        assertTrue(registerResponse.validateCode(ResponseCode.REGISTRATION_FAILED));
    }

    // Register with duplicate ID
    @Test
    void registerTestIdUsed(){

        Map<String, String> registerRequest = Map.of(
                "shopId", "testshop1",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertFalse(registerResponse.isSuccess());
        assertTrue(registerResponse.validateCode(ResponseCode.REGISTRATION_FAILED));
    }

    // Login successfully
    @Test
    void loginTest() {

        // call the endpoint with type reference
        ResponseEntity<ResponseUtil<Map<String, Object>>> response = restTemplate.exchange(
                "/auth/login?shopId=testshop1&password=password123",
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
        ResponseUtil<?> loginResponse = restTemplate.postForObject("/auth/login?shopId=notashop&password=mypassword", null, ResponseUtil.class);

        assertFalse(loginResponse.isSuccess());
        assertTrue(loginResponse.validateCode(ResponseCode.LOGIN_FAILED));
    }

    // Login with invalid password
    @Test
    void loginTestPassword(){


        ResponseUtil<?> loginResponse = restTemplate.postForObject("/auth/login?shopId=testshop1&password=password", null, ResponseUtil.class);

        assertFalse(loginResponse.isSuccess());
        assertTrue(loginResponse.validateCode(ResponseCode.LOGIN_FAILED));
    }

    // Admin Login
    @AfterAll
    void adminTest(){

        ResponseEntity<ResponseUtil<Map<String, Object>>> response = restTemplate.exchange(
                "/auth/admin?adminId=admin&adminPassword=123guesswhat",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<ResponseUtil<Map<String, Object>>>() {}
        );

        ResponseUtil<Map<String, Object>> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());

        Map<String, Object> data = body.getData();
        assertNotNull(data.get("token"));
        assertNotNull(data.get("adminId"));

        //delete test data
        String token = (String) data.get("token"); // cast to String
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> deleteResponse = restTemplate.exchange(
                "/shops/testshop1/admin",
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyDelete = deleteResponse.getBody();
        assertNotNull(bodyDelete);
        assertTrue(bodyDelete.isSuccess());
    }

    // Login with invalid admin id
    @Test
    void adminTestId(){

        ResponseUtil<?> loginResponse = restTemplate.postForObject("/auth/admin?adminId=adm&adminPassword=123guesswhat", null, ResponseUtil.class);

        assertFalse(loginResponse.isSuccess());
        assertTrue(loginResponse.validateCode(ResponseCode.LOGIN_FAILED));
    }

    // Login with invalid admin password
    @Test
    void adminTestPassword(){

        ResponseUtil<?> loginResponse = restTemplate.postForObject("/auth/admin?adminId=admin&adminPassword=guesswhat", null, ResponseUtil.class);

        assertFalse(loginResponse.isSuccess());
        assertTrue(loginResponse.validateCode(ResponseCode.LOGIN_FAILED));
    }
}
