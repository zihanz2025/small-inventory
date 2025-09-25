package com.zihan.small_inventory.inventory.items;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;

import java.util.UUID;

//Product class
@DynamoDbBean
public class Product {

    private String productId;    // PK
    private String shopId;       // link to Shop
    private String categoryId;   // link to Category
    private String name;
    private Integer stock;
    private Double price;
    private Boolean forSale;     // false if reward-only
    private String photoUrl;

    public Product() {}

    public static Product newProduct(
            String shopId,
            String categoryId,
            String name,
            Integer stock,
            Double price,
            Boolean forSale,
            String photoUrl        // optional, can be null
    ) {
        Product product = new Product();
        product.productId = UUID.randomUUID().toString();
        product.shopId = shopId;
        product.categoryId = categoryId;
        product.name = name;
        product.stock = stock;
        product.price = price;
        product.forSale = forSale;
        // photoUrl optional, can be null
        product.photoUrl = photoUrl;

        return product;
    }


    @DynamoDbPartitionKey
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "shopId-categoryId-index")
    public String getShopId() { return shopId; }
    public void setShopId(String shopId) { this.shopId = shopId; }

    @DynamoDbSecondarySortKey(indexNames = "shopId-categoryId-index")
    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Boolean getForSale() { return forSale; }
    public void setForSale(Boolean forSale) { this.forSale = forSale; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }
}
