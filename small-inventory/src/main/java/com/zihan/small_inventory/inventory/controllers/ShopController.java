package com.zihan.small_inventory.inventory.controllers;

import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.services.ShopService;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // Get a shop by ID
    @GetMapping("/{shopId}")
    public ResponseUtil<?> getShop(@PathVariable String shopId) {
        return shopService.getShop(shopId);
    }

    @PutMapping("/{shopId}/name")
    public ResponseUtil<?> updateShopName(
            @PathVariable String shopId,
            @RequestParam String newName) {
        return shopService.updateShopName(shopId, newName);
    }

    @PutMapping("/{shopId}/password")
    public ResponseUtil<Shop> updatePassword(
            @PathVariable String shopId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        return shopService.updatePassword(shopId, oldPassword, newPassword);
    }

    @DeleteMapping("/{shopId}")
    public ResponseUtil<String> deleteOwnShop(@PathVariable String shopId, @RequestParam String password) {
        return shopService.deleteOwnShop(shopId,password);
    }

    @DeleteMapping("/{shopId}/admin")
    public ResponseUtil<String> deleteShop(@PathVariable String shopId) {
        return shopService.deleteShop(shopId);
    }
}
