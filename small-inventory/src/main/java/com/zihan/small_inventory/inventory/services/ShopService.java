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

    public Optional<Shop> getShop(String shopId) {
        return shopRepository.findByShopId(shopId);
    }
}
