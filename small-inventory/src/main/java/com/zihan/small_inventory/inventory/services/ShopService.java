package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.exceptions.ShopIdAlreadyExistsException;
import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.repositories.ShopRepository;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final PasswordEncoder passwordEncoder;
    private final DynamoDbClient dynamoDbClient;

    public ShopService(ShopRepository shopRepository, DynamoDbClient dynamoDbClient) {
        this.shopRepository = shopRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.dynamoDbClient = dynamoDbClient;
    }

    public ResponseUtil<Shop> createShop(Shop shop) {

        if (shop.getShopId() == null || shop.getShopId().isBlank() || shop.getShopId().length() <8) {
            return new ResponseUtil<>("Shop ID must be at least 8 characters.");
        }

        if (shop.getOwnerPassword() == null || shop.getOwnerPassword().length() < 8) {
            return new ResponseUtil<>("Password must be at least 8 characters.");
        }

        if (shopRepository.shopIdExists(shop.getShopId())) {
            return new ResponseUtil<>("Shop ID already exists.");
        }
        // hash the raw password and replace it
        String hashedPassword = passwordEncoder.encode(shop.getOwnerPassword());
        shop.setOwnerPassword(hashedPassword);

        Shop newShop = Shop.newShop(
                shop.getShopId(),
                shop.getShopName(),
                shop.getOwnerEmail(),
                shop.getOwnerPassword()
        );

        Shop savedShop = shopRepository.save(newShop);
        return new ResponseUtil<>(savedShop);
    }

    public Optional<Shop> getShop(String shopId) {
        return shopRepository.findByShopId(shopId);
    }
}
