package com.zihan.small_inventory.inventory.items;

import com.fasterxml.jackson.annotation.JsonProperty;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.util.UUID;

//
// Each Shop class represents a shop where only the user can access and modify inventory information.
//

@DynamoDbBean
public class Shop {

    private String keyId;
    private String shopId;          // PK, auto-generated
    private String shopName;
    private String ownerEmail;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String ownerPassword; // store hashed password

    // -----------------------------
    // Constructors
    // -----------------------------

    // default constructor needed by DynamoDB Enhanced Client
    public Shop() {}

    // Factory method to create new shop with IDs generated
    public static Shop newShop(String shopId, String shopName, String ownerEmail, String ownerPassword) {
        Shop shop = new Shop();
        shop.keyId = UUID.randomUUID().toString();   // generate unique ID
        shop.shopId = shopId;
        shop.shopName = shopName;
        shop.ownerEmail = ownerEmail;
        shop.ownerPassword = ownerPassword;
        return shop;
    }

    // -----------------------------
    // Getters & Setters
    // -----------------------------
    @DynamoDbPartitionKey
    public String getKeyId() {return keyId;}
    public void setKeyId(String keyId) {this.keyId = keyId;}

    @DynamoDbSecondaryPartitionKey(indexNames = "shopId")
    public String getShopId() {return shopId;}
    public void setShopId(String shopId) {this.shopId = shopId;}

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getOwnerEmail() { return ownerEmail; }
    public void setOwnerEmail(String ownerEmail) { this.ownerEmail = ownerEmail; }

    public String getOwnerPassword() { return ownerPassword; }
    public void setOwnerPassword(String ownerPasswordHash) { this.ownerPassword = ownerPasswordHash; }

}
