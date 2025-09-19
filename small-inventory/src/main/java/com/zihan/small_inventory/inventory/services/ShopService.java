package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.exceptions.ShopIdAlreadyExistsException;
import com.zihan.small_inventory.inventory.items.Shop;
import com.zihan.small_inventory.inventory.repositories.ShopRepository;
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

    public List<String> listTables() {
        return dynamoDbClient.listTables().tableNames();
    }

    public Shop createShop(Shop shopInput) {

        if (shopRepository.shopIdExists(shopInput.getShopId())) {
            throw new ShopIdAlreadyExistsException(shopInput.getShopId());
        }
        // hash the raw password and replace it
        String hashedPassword = passwordEncoder.encode(shopInput.getOwnerPassword());
        shopInput.setOwnerPassword(hashedPassword);

        // ensure shopId and createdAt are set
        Shop newShop = Shop.newShop(
                shopInput.getShopId(),
                shopInput.getShopName(),
                shopInput.getOwnerEmail(),
                shopInput.getOwnerPassword()
        );

        return shopRepository.save(newShop);
    }

    public Optional<Shop> getShop(String shopId) {
        return shopRepository.findById(shopId);
    }
}
