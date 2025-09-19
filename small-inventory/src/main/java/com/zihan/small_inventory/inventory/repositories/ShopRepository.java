package com.zihan.small_inventory.inventory.repositories;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import com.zihan.small_inventory.inventory.items.Shop;

import java.util.Optional;

@Repository
public class ShopRepository {

    private final DynamoDbTable<Shop> shopTable;
    private final DynamoDbClient dynamoDbClient;

    public ShopRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;

        ensureTableExists();

        this.shopTable = enhancedClient.table("Shop", TableSchema.fromBean(Shop.class));
    }

    private void ensureTableExists() {
        String tableName = "Shop";

        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build());
            System.out.println("Table already exists: " + tableName);
        } catch (ResourceNotFoundException e) {
            System.out.println("Table not found. Creating: " + tableName);
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(
                            KeySchemaElement.builder()
                                    .attributeName("keyId")  // Primary key
                                    .keyType(KeyType.HASH)
                                    .build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder()
                                    .attributeName("keyId")
                                    .attributeType(ScalarAttributeType.S)
                                    .build(),
                            AttributeDefinition.builder()
                                    .attributeName("shopId") // GSI
                                    .attributeType(ScalarAttributeType.S)
                                    .build()
                    )
                    .globalSecondaryIndexes(
                            GlobalSecondaryIndex.builder()
                                    .indexName("shopId")
                                    .keySchema(KeySchemaElement.builder()
                                            .attributeName("shopId")
                                            .keyType(KeyType.HASH)
                                            .build())
                                    .projection(Projection.builder()
                                            .projectionType(ProjectionType.ALL)
                                            .build())
                                    .provisionedThroughput(ProvisionedThroughput.builder()
                                            .readCapacityUnits(5L)
                                            .writeCapacityUnits(5L)
                                            .build())
                                    .build()
                    )
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();

            dynamoDbClient.createTable(request);
            System.out.println("Table created: " + tableName);

            // Optionally wait until table is ACTIVE
            dynamoDbClient.waiter().waitUntilTableExists(
                    DescribeTableRequest.builder().tableName(tableName).build()
            );
        }
    }

    public Shop save(Shop shop) {
        shopTable.putItem(shop);
        return shop;
    }

    public Optional<Shop> findById(String keyId) {
        return Optional.ofNullable(
                shopTable.getItem(r -> r.key(k -> k.partitionValue(keyId)))
        );
    }

    // New method to find by shopId using GSI
    public Optional<Shop> findByShopId(String shopId) {
        // Assume you have an index on shopId
        var index = shopTable.index("shopId");

        QueryConditional query = QueryConditional.keyEqualTo(k -> k.partitionValue(shopId));

        // Use SDK iterable and flatMap to get the first matching item
        return index.query(query)
                .stream()
                .flatMap(page -> page.items().stream())
                .findFirst();
    }

    public boolean shopIdExists(String shopId) {
        var index = shopTable.index("shopId");

        QueryConditional query = QueryConditional.keyEqualTo(k -> k.partitionValue(shopId));

        // Just check if there's at least one page with items
        return index.query(query)
                .stream()
                .anyMatch(page -> !page.items().isEmpty());
    }
}

