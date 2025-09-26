package com.zihan.small_inventory.inventory.controllers;

import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.services.AuthService;
import com.zihan.small_inventory.inventory.services.ShopService;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for authentication-related endpoints.
 */

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {this.authService = authService;}

    /**
     * Register a new shop.
     */
    @PostMapping("/register")
    public ResponseUtil<Shop> register(@RequestBody Shop shop) {
        return authService.registerShop(shop);
    }
    /**
     * Login in as shop owner.
     */
    @PostMapping("/login")
    public ResponseUtil<Map<String, Object>> login(@RequestParam String shopId, @RequestParam String password) {
        return authService.loginShop(shopId, password);
    }

    /**
     * Admin login.
     */
    @PostMapping("/admin")
    public ResponseUtil<Map<String, Object>> loginAdmin(@RequestParam String adminId, @RequestParam String adminPassword) {
        return authService.loginAdmin(adminId, adminPassword);
    }

}
