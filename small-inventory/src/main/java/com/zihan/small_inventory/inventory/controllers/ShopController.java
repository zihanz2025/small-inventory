package com.zihan.small_inventory.inventory.controllers;

import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.services.ShopService;
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

    @GetMapping("/test")
    public ResponseEntity<String> testDbConnection() {
        try {
            // Try to list tables in DynamoDB
            List<String> tableNames = shopService.listTables();
            return ResponseEntity.ok("DynamoDB is reachable. Tables: " + tableNames);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Failed to connect to DynamoDB: " + e.getMessage());
        }
    }


    // Create a new shop (frontend passes raw password inside Shop)
    @PostMapping
    public ResponseEntity<Shop> createShop(@RequestBody Shop shopInput) {
        System.out.println("Owner password: " + shopInput.getOwnerPassword());
        Shop createdShop = shopService.createShop(shopInput);
        return ResponseEntity.ok(createdShop);
    }

    // Get a shop by ID
    @GetMapping("/{id}")
    public ResponseEntity<Shop> getShop(@PathVariable String id) {
        return shopService.getShop(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
