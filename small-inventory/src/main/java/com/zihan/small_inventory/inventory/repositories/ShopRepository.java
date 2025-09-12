package com.zihan.small_inventory.inventory.repositories;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import com.zihan.small_inventory.inventory.items.Shop;

import java.util.Optional;

public class ShopRepository {

    private final DynamoDbTable<Shop> shopTable;

    public ShopRepository(DynamoDbEnhancedClient client) {
        this.shopTable = client.table("Shop", TableSchema.fromBean(Shop.class));
    }

    public void save(Shop shop) { shopTable.putItem(shop); }

    public Optional<Shop> findById(String shopId) {
        return Optional.ofNullable(shopTable.getItem(r -> r.key(k -> k.partitionValue(shopId))));
    }
}

