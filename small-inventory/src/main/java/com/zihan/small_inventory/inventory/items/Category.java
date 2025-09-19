package com.zihan.small_inventory.inventory.items;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.UUID;

@DynamoDbBean
public class Category {

    private String shopId;      // Partition key
    private String categoryId;  // Sort key
    private String name;

    public Category() {}

    // Factory method
    public static Category newCategory(String shopId, String name) {
        Category category = new Category();
        category.shopId = shopId;
        category.categoryId = UUID.randomUUID().toString();
        category.name = name;
        return category;
    }

    @DynamoDbPartitionKey
    public String getShopId() { return shopId; }
    public void setShopId(String shopId) { this.shopId = shopId; }

    @DynamoDbSortKey
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
