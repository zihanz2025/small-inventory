package com.zihan.small_inventory.inventory.items;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.UUID;

//
// Each Shop class represents a shop where only the user can access and modify inventory information.
//

@DynamoDbBean
public class Shop {

    private String shopId;          // PK, auto-generated
    private String shopName;
    private String ownerEmail;
    private String ownerPasswordHash; // store hashed password
    private long createdAt;         // timestamp

    // -----------------------------
    // Constructors
    // -----------------------------

    // default constructor needed by DynamoDB Enhanced Client
    public Shop() {}

    // Factory method to create new shop with IDs generated
    public static Shop newShop(String shopName, String ownerEmail, String ownerPasswordHash) {
        Shop shop = new Shop();
        shop.shopId = UUID.randomUUID().toString();   // generate unique ID
        shop.shopName = shopName;
        shop.ownerEmail = ownerEmail;
        shop.ownerPasswordHash = ownerPasswordHash;
        shop.createdAt = System.currentTimeMillis();
        return shop;
    }

    // -----------------------------
    // DynamoDB Key
    // -----------------------------
    @DynamoDbPartitionKey
    public String getShopId() {return shopId;}
    public void setShopId(String shopId) {this.shopId = shopId;}

    // -----------------------------
    // Getters & Setters
    // -----------------------------
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getOwnerPasswordHash() { return ownerPasswordHash; }
    public void setOwnerPasswordHash(String ownerPasswordHash) { this.ownerPasswordHash = ownerPasswordHash; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
