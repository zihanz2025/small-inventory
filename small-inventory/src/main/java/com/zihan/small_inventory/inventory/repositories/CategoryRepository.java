package com.zihan.small_inventory.inventory.repositories;

import com.zihan.small_inventory.inventory.items.Category;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbTable<Category> categoryTable;
    private final String tableName = "Category";

    public CategoryRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
        ensureTableExists();
        this.categoryTable = enhancedClient.table(tableName, TableSchema.fromBean(Category.class));

    }

    private void ensureTableExists() {
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
                            KeySchemaElement.builder().attributeName("shopId").keyType(KeyType.HASH).build(),
                            KeySchemaElement.builder().attributeName("categoryId").keyType(KeyType.RANGE).build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder().attributeName("shopId").attributeType(ScalarAttributeType.S).build(),
                            AttributeDefinition.builder().attributeName("categoryId").attributeType(ScalarAttributeType.S).build()
                    )
                    .provisionedThroughput(ProvisionedThroughput.builder()
                            .readCapacityUnits(5L)
                            .writeCapacityUnits(5L)
                            .build())
                    .build();

            dynamoDbClient.createTable(request);
            System.out.println("Table created: " + tableName);

            dynamoDbClient.waiter().waitUntilTableExists(
                    DescribeTableRequest.builder().tableName(tableName).build()
            );
        }
    }

    public Category save(Category category) {
        categoryTable.putItem(category);
        return category;
    }

    // Find all categories for a given shopId
    public List<Category> findByShopId(String shopId) {
        return categoryTable.query(r -> r.queryConditional(
                        QueryConditional.keyEqualTo(Key.builder().partitionValue(shopId).build())
                ))
                .items()
                .stream()
                .toList();
    }

    public Optional<Category> findById(String categoryId) {
        // Since categoryId is the sort key, we need to scan to find it
        Expression expression = Expression.builder()
                .expression("categoryId = :categoryId")
                .putExpressionValue(":categoryId", AttributeValue.builder().s(categoryId).build())
                .build();

        return categoryTable.scan(r -> r.filterExpression(expression))
                .items()
                .stream()
                .findFirst();
    }

    public boolean findByShopIdAndName(String shopId, String name) {
        Expression expression = Expression.builder()
                .expression("shopId = :shopId AND #n = :name")
                .putExpressionValue(":shopId", AttributeValue.builder().s(shopId).build())
                .putExpressionValue(":name", AttributeValue.builder().s(name).build())
                .putExpressionName("#n", "name")
                .build();

        return categoryTable.scan(r -> r.filterExpression(expression))
                .items()
                .stream()
                .findAny()
                .isPresent();
    }

    // Delete a category by shopId and categoryId
    public void delete(String shopId, String categoryId) {
        categoryTable.deleteItem(r -> r.key(
                k -> k.partitionValue(shopId).sortValue(categoryId)
        ));
    }

}
