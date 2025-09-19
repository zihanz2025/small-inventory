package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.inventory.items.Product;
import com.zihan.small_inventory.inventory.repositories.ProductRepository;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create a product
    public ResponseUtil<Product> createProduct(Product product) {
        // Validate fields
        String validationError = validateProduct(product);
        if (validationError != null) {
            return new ResponseUtil<>(validationError);
        }

        Product saved = productRepository.save(product);
        return new ResponseUtil<>(saved);
    }

    // Get all products for a shop and category
    public ResponseUtil<List<Product>> getProductsByShopAndCategory(String shopId, String categoryId) {
        return new ResponseUtil<>(productRepository.findByShopIdAndCategoryId(shopId, categoryId));
    }

    // Get product by ID
    public ResponseUtil<Optional<Product>> getProductById(String productId) {
        return new ResponseUtil<>(productRepository.findById(productId));
    }

    // Update product
    public ResponseUtil<Product> updateProduct(Product product) {
        Optional<Product> existing = productRepository.findById(product.getProductId());
        if (existing.isEmpty()) {
            return new ResponseUtil<>("Product not found");
        }

        String validationError = validateProduct(product);
        if (validationError != null) {
            return new ResponseUtil<>(validationError);
        }

        Product updated = productRepository.save(product);
        return new ResponseUtil<>(updated);
    }

    // Delete product
    public ResponseUtil<Void> deleteProduct(String productId) {
        Optional<Product> existing = productRepository.findById(productId);
        if (existing.isEmpty()) {
            return new ResponseUtil<>("Product not found");
        }

        productRepository.delete(productId);
        return new ResponseUtil<>((Void) null);
    }

    // Helper to validate product fields
    private String validateProduct(Product product) {
        if (product.getStock() < 0) {
            return "Stock cannot be negative";
        }
        if (product.getPrice() < 0) {
            return "Price cannot be negative";
        }
        if (product.isForSale() != true && product.isForSale() != false) { // redundant in Java, but for completeness
            return "Please define product type";
        }
        return null;
    }
}
