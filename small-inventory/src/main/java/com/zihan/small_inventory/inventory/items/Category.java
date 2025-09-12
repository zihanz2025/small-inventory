package com.zihan.small_inventory.inventory.items;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.UUID;

//Category class
@DynamoDbBean
public class Category {

    public static final String DEFAULT_CATEGORY_ID = "UNLABELED"; // can also generate UUID once
    private String categoryId;   // PK
    private String shopId;       // link to Shop
    private String name;


    // Default constructor needed by DynamoDB
    public Category() {}

    // Factory method
    public static Category newCategory(String shopId, String name) {
        Category category = new Category();
        category.categoryId = UUID.randomUUID().toString();
        category.shopId = shopId;
        category.name = name;
        return category;
    }

    @DynamoDbPartitionKey
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getShopId() { return shopId; }
    public void setShopId(String shopId) { this.shopId = shopId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}
