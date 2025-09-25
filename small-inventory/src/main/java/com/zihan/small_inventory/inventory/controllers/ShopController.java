package com.zihan.small_inventory.inventory.controllers;

import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.services.ShopService;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    // Get a shop by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShop(@PathVariable String id) {
        return shopService.getShop(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
