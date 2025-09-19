package com.zihan.small_inventory.inventory.repositories;

import com.zihan.small_inventory.inventory.items.Product;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final DynamoDbClient dynamoDbClient;
    private final DynamoDbTable<Product> productTable;
    private final String tableName = "Product";

    public ProductRepository(DynamoDbEnhancedClient enhancedClient, DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
        ensureTableExists();
        this.productTable = enhancedClient.table(tableName, TableSchema.fromBean(Product.class));
    }

    private void ensureTableExists() {
        try {
            dynamoDbClient.describeTable(DescribeTableRequest.builder().tableName(tableName).build());
            System.out.println("Table already exists: " + tableName);
        } catch (ResourceNotFoundException e) {
            System.out.println("Table not found. Creating: " + tableName);
            CreateTableRequest request = CreateTableRequest.builder()
                    .tableName(tableName)
                    .keySchema(
                            KeySchemaElement.builder().attributeName("productId").keyType(KeyType.HASH).build()
                    )
                    .attributeDefinitions(
                            AttributeDefinition.builder().attributeName("productId").attributeType(ScalarAttributeType.S).build(),
                            AttributeDefinition.builder().attributeName("shopId").attributeType(ScalarAttributeType.S).build(),
                            AttributeDefinition.builder().attributeName("categoryId").attributeType(ScalarAttributeType.S).build()
                    )
                    .globalSecondaryIndexes(
                            GlobalSecondaryIndex.builder()
                                    .indexName("shopId-categoryId-index")
                                    .keySchema(
                                            KeySchemaElement.builder().attributeName("shopId").keyType(KeyType.HASH).build(),
                                            KeySchemaElement.builder().attributeName("categoryId").keyType(KeyType.RANGE).build()
                                    )
                                    .projection(Projection.builder().projectionType(ProjectionType.ALL).build())
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

            dynamoDbClient.waiter().waitUntilTableExists(
                    DescribeTableRequest.builder().tableName(tableName).build()
            );
        }
    }

    public Product save(Product product) {
        productTable.putItem(product);
        return product;
    }

    public Optional<Product> findById(String productId) {
        return Optional.ofNullable(productTable.getItem(r -> r.key(k -> k.partitionValue(productId))));
    }

    public List<Product> findByShopIdAndCategoryId(String shopId, String categoryId) {
        var index = productTable.index("shopId-categoryId-index");

        QueryConditional query = QueryConditional.keyEqualTo(k -> k.partitionValue(shopId).sortValue(categoryId));

        // Flatten pages into a list of products
        List<Product> products = index.query(query).stream()
                .flatMap(page -> page.items().stream())
                .toList();

        return products;
    }


    public void delete(String productId) {
        productTable.deleteItem(r -> r.key(k -> k.partitionValue(productId)));
    }
}
