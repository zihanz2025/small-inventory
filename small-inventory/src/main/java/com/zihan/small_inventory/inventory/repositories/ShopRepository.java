package com.zihan.small_inventory.inventory.repositories;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import com.zihan.small_inventory.inventory.items.Shop;

import java.util.Optional;

@Repository
public class ShopRepository {

    private final DynamoDbTable<Shop> shopTable;

    public ShopRepository(DynamoDbEnhancedClient client) {
        this.shopTable = client.table("Shop", TableSchema.fromBean(Shop.class));
    }

    public Shop save(Shop shop) {
        shopTable.putItem(shop);
        return shop;
    }

    public Optional<Shop> findById(String shopId) {
        return Optional.ofNullable(shopTable.getItem(r -> r.key(k -> k.partitionValue(shopId))));
    }
}

