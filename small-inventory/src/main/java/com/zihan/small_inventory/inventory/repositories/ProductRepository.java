package com.zihan.small_inventory.inventory.repositories;

import com.zihan.small_inventory.inventory.items.Product;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.Optional;

public class ProductRepository {

    private final DynamoDbTable<Product> productTable;

    public ProductRepository(DynamoDbEnhancedClient enhancedClient) {
        this.productTable = enhancedClient.table("Product", TableSchema.fromBean(Product.class));
    }

    public void save(Product product) {
        productTable.putItem(product);
    }

    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(productTable.getItem(r -> r.key(k -> k.partitionValue(productId))));
    }

    public void delete(String productId) {
        productTable.deleteItem(r -> r.key(k -> k.partitionValue(productId)));
    }

    // Add other queries (e.g., find all products by shopId or categoryId)
}

