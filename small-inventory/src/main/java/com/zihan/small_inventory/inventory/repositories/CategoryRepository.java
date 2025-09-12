package com.zihan.small_inventory.inventory.repositories;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import com.zihan.small_inventory.inventory.items.Category;

import java.util.Optional;

public class CategoryRepository {

    private final DynamoDbTable<Category> categoryTable;

    public CategoryRepository(DynamoDbEnhancedClient client) {
        this.categoryTable = client.table("Shop", TableSchema.fromBean(Category.class));
    }

    public void save(Category category) { categoryTable.putItem(category); }

    public Optional<Category> findById(String shopId) {
        return Optional.ofNullable(categoryTable.getItem(r -> r.key(k -> k.partitionValue(shopId))));
    }
}