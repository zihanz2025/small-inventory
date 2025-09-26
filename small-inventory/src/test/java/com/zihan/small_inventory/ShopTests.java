package com.zihan.small_inventory;

import com.zihan.small_inventory.constants.ResponseCode;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
public class ShopTests {
    @Autowired
    private TestRestTemplate restTemplate;
    private String tokenUser, tokenUser2, tokenAdmin;

    // Register and Login
    @BeforeAll
    void registerAndLogin() {

        // Register testshop1
        Map<String, String> registerRequest = Map.of(
                "shopId", "testshop1",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse = restTemplate.postForObject("/auth/register", registerRequest, ResponseUtil.class);
        assertTrue(registerResponse.isSuccess());

        // Login testshop1
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
        tokenUser = (String)data.get("token");
        System.out.println("Owner Token 1: " + data.get("shopId"));

        // Register testshop2
        Map<String, String> registerRequest2 = Map.of(
                "shopId", "testshop2",
                "shopName","My First Shop",
                "ownerEmail", "TestEmail@email.com",
                "ownerPassword", "password123"
        );
        ResponseUtil<?> registerResponse2 = restTemplate.postForObject("/auth/register", registerRequest2, ResponseUtil.class);
        assertTrue(registerResponse2.isSuccess());

        // Login testshop2
        ResponseEntity<ResponseUtil<Map<String, Object>>> response2 = restTemplate.exchange(
                "/auth/login?shopId=testshop2&password=password123",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<ResponseUtil<Map<String, Object>>>() {}
        );

        ResponseUtil<Map<String, Object>> body2 = response2.getBody();
        assertNotNull(body2);
        assertTrue(body2.isSuccess());

        Map<String, Object> data2 = body2.getData();
        assertNotNull(data2.get("token"));
        assertNotNull(data2.get("shopId"));
        tokenUser2 = (String)data2.get("token");
        System.out.println("Owner Token 2: " + data2.get("shopId"));

        // Login admin
        ResponseEntity<ResponseUtil<Map<String, Object>>> adminResponse = restTemplate.exchange(
                "/auth/admin?adminId=admin&adminPassword=123guesswhat",
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<ResponseUtil<Map<String, Object>>>() {}
        );

        ResponseUtil<Map<String, Object>> bodyAdmin = adminResponse.getBody();
        assertNotNull(bodyAdmin);
        assertTrue(bodyAdmin.isSuccess());

        Map<String, Object> dataAdmin = bodyAdmin.getData();
        assertNotNull(dataAdmin.get("token"));
        assertNotNull(dataAdmin.get("adminId"));

        tokenAdmin = (String) dataAdmin.get("token");
        System.out.println("Admin Token: " + tokenAdmin);
        System.out.println("Owner Token 2: " + dataAdmin.get("adminId"));
    }

    // Get shop
    @Test
    void getShopTest(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> getResponse = restTemplate.exchange(
                "/shops/testshop1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> body = getResponse.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
    }

    // Get shop without token
    @Test
    void getShopTestNoToken(){

        ResponseEntity<ResponseUtil<Void>> getResponse = restTemplate.exchange(
                "/shops/testshop1",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> body = getResponse.getBody();
        assertFalse(body.isSuccess());
        assertTrue(body.validateCode(ResponseCode.AUTH_TOKEN_FAILED));
    }

    // Get shop with wrong token
    @Test
    void getShopTestWrongToken(){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser2);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> getResponse = restTemplate.exchange(
                "/shops/testshop1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> body = getResponse.getBody();
        assertFalse(body.isSuccess());
        assertTrue(body.validateCode(ResponseCode.AUTH_ROLE_FAILED));
    }

    // Get shop as admin
    @Test
    void getShopTestAdmin(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenAdmin);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> getResponse = restTemplate.exchange(
                "/shops/testshop1",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> body = getResponse.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
    }

    // Get shop invalid shop id
    @Test
    void getShopTestInvalidId(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenAdmin);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> getResponse = restTemplate.exchange(
                "/shops/notashop",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> body = getResponse.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertTrue(body.validateCode(ResponseCode.SHOP_ID_NOT_FOUND));
    }

    // Update Name
    @Test
    void updateNameTest(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> updateResponse = restTemplate.exchange(
                "/shops/testshop1/name?newName=New Name",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyUpdated = updateResponse.getBody();
        assertNotNull(bodyUpdated);
        assertTrue(bodyUpdated.isSuccess());
    }

    // Update Name without token
    @Test
    void updateNameTestNoToken(){

        ResponseEntity<ResponseUtil<Void>> updateResponse = restTemplate.exchange(
                "/shops/testshop1/name?newName=New Name2",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyUpdated = updateResponse.getBody();
        assertFalse(bodyUpdated.isSuccess());
        assertTrue(bodyUpdated.validateCode(ResponseCode.AUTH_TOKEN_FAILED));
    }

    // Update Name with wrong token
    @Test
    void updateNameTestWrongToken(){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser2);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> updateResponse = restTemplate.exchange(
                "/shops/testshop1/name?newName=New Name2",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyUpdated = updateResponse.getBody();
        assertFalse(bodyUpdated.isSuccess());
        assertTrue(bodyUpdated.validateCode(ResponseCode.AUTH_ROLE_FAILED));
    }


    // Update password
    @Test
    void updatePasswordTest(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> updateResponse = restTemplate.exchange(
                "/shops/testshop1/password?oldPassword=password123&newPassword=guesswhat123",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyUpdated = updateResponse.getBody();
        assertNotNull(bodyUpdated);
        assertTrue(bodyUpdated.isSuccess());
    }

    // Update password without token
    @Test
    void updatePasswordTestNoToken(){

        ResponseEntity<ResponseUtil<Void>> updateResponse = restTemplate.exchange(
                "/shops/testshop1/password?oldPassword=password123&newPassword=guesswhat123",
                HttpMethod.PUT,
                null,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyUpdated = updateResponse.getBody();
        assertNotNull(bodyUpdated);
        assertFalse(bodyUpdated.isSuccess());
        assertTrue(bodyUpdated.validateCode(ResponseCode.AUTH_TOKEN_FAILED));
    }

    // Update password with wrong old password
    @Test
    void updatePasswordTestWrongPassword(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser2);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> updateResponse = restTemplate.exchange(
                "/shops/testshop2/password?oldPassword=password&newPassword=guesswhat123",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyUpdated = updateResponse.getBody();
        assertNotNull(bodyUpdated);
        assertFalse(bodyUpdated.isSuccess());
        assertTrue(bodyUpdated.validateCode(ResponseCode.SHOP_INVALID_PASSWORD));
    }

    // Update password with invalid new password
    @Test
    void updatePasswordTestInvalidPassword(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser2);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> updateResponse = restTemplate.exchange(
                "/shops/testshop2/password?oldPassword=password123&newPassword=guess",
                HttpMethod.PUT,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyUpdated = updateResponse.getBody();
        assertNotNull(bodyUpdated);
        assertFalse(bodyUpdated.isSuccess());
        assertTrue(bodyUpdated.validateCode(ResponseCode.SHOP_UPDATE_FAILED));
    }

    //Delete own shop without token
    void deleteTestNoToken(){

        ResponseEntity<ResponseUtil<Void>> deleteResponse = restTemplate.exchange(
                "/shops/testshop1?password=password123",
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyDelete = deleteResponse.getBody();
        assertNotNull(bodyDelete);
        assertFalse(bodyDelete.isSuccess());
        assertTrue(bodyDelete.validateCode(ResponseCode.AUTH_TOKEN_FAILED));
    }

    //Delete own shop with wrong token
    void deleteTestWrongToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser2);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> deleteResponse = restTemplate.exchange(
                "/shops/testshop1?password=guesswhat123",
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyDelete = deleteResponse.getBody();
        assertNotNull(bodyDelete);
        assertFalse(bodyDelete.isSuccess());
        assertTrue(bodyDelete.validateCode(ResponseCode.AUTH_ROLE_FAILED));
    }

    //Delete own shop with wrong password
    void deleteTestWrongPassword(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> deleteResponse = restTemplate.exchange(
                "/shops/testshop1?password=password123",
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyDelete = deleteResponse.getBody();
        assertNotNull(bodyDelete);
        assertFalse(bodyDelete.isSuccess());
        assertTrue(bodyDelete.validateCode(ResponseCode.SHOP_INVALID_PASSWORD));
    }

    // Admin delete as user
    @Test
    void deleteTestUserAdmin(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenUser);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<ResponseUtil<Void>> deleteResponse = restTemplate.exchange(
                "/shops/testshop1/admin",
                HttpMethod.DELETE,
                requestEntity,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );

        ResponseUtil<Void> bodyDelete = deleteResponse.getBody();
        assertNotNull(bodyDelete);
        assertFalse(bodyDelete.isSuccess());
        assertTrue(bodyDelete.validateCode(ResponseCode.AUTH_ROLE_FAILED));
    }

     //Admin clean-up & delete test
    @AfterAll
    void deleteAll(){

        //admin delete testshop1
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + tokenAdmin);

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

        //testshop2 deletes own shop
        HttpHeaders headers2 = new HttpHeaders();
        headers2.set("Authorization", "Bearer " + tokenUser2);

        HttpEntity<Void> requestEntity2 = new HttpEntity<>(headers2);
        ResponseEntity<ResponseUtil<Void>> deleteResponse2 = restTemplate.exchange(
                "/shops/testshop2?password=password123",
                HttpMethod.DELETE,
                requestEntity2,
                new ParameterizedTypeReference<ResponseUtil<Void>>() {}
        );
        ResponseUtil<Void> bodyDelete2 = deleteResponse2.getBody();
        assertNotNull(bodyDelete2);
        assertTrue(bodyDelete2.isSuccess());
    }
}
