package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.inventory.items.Category;
import com.zihan.small_inventory.inventory.items.Product;
import com.zihan.small_inventory.inventory.repositories.CategoryRepository;
import com.zihan.small_inventory.inventory.repositories.ProductRepository;
import com.zihan.small_inventory.inventory.repositories.ShopRepository;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ShopRepository shopRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
    }

    // Create a product
    public ResponseUtil<Product> createProduct(Product product) {

        // Check shop exists
        if (shopRepository.findByShopId(product.getShopId()).isEmpty()) {
            return new ResponseUtil<>("Invalid shopId: shop does not exist.");
        }

        // Check category exists and belongs to this shop
        Optional<Category> categoryOpt = categoryRepository.findById(product.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return new ResponseUtil<>("Invalid categoryId: category does not exist.");
        }

        if (!categoryOpt.get().getShopId().equals(product.getShopId())) {
            return new ResponseUtil<>("Invalid categoryId: category does not exist in the given shop.");
        }
        // Validate fields
        String validationError = validateProduct(product);
        if (validationError != null) {
            return new ResponseUtil<>(validationError);
        }
        product = Product.newProduct(
                product.getShopId(),
                product.getCategoryId(),
                product.getName(),
                product.getStock(),
                product.getPrice(),
                product.getForSale(),
                product.getPhotoUrl()
        );
        Product saved = productRepository.save(product);
        return new ResponseUtil<>(saved);
    }

    // Get all products for a shop and category
    public ResponseUtil<List<Product>> getProductsByShopAndCategory(String shopId, String categoryId) {
        return new ResponseUtil<>(productRepository.findByShopIdAndCategoryId(shopId, categoryId));
    }

    // Get product by ID
    public ResponseUtil<Product> getProductById(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            return new ResponseUtil<>("Product not found.");
        }
        return new ResponseUtil<>(product.get());
    }
    // Update product
    public ResponseUtil<Product> updateProduct(Product product) {
        Optional<Product> existing = productRepository.findById(product.getProductId());
        if (existing.isEmpty()) {
            return new ResponseUtil<>("Product not found");
        }
        // Check category exists and belongs to this shop
        Optional<Category> categoryOpt = categoryRepository.findById(product.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return new ResponseUtil<>("Invalid categoryId: category does not exist.");
        }
        System.out.println(categoryOpt.get().getShopId());
        System.out.println(product.getShopId());
        if (!categoryOpt.get().getShopId().equals(product.getShopId())) {
            return new ResponseUtil<>("Invalid categoryId: category does not exist in the given shop.");
        }

        String validationError = validateProduct(product);
        if (validationError != null) {
            return new ResponseUtil<>(validationError);
        }

        Product updated = existing.get();

        updated.setName(product.getName());
        updated.setStock(product.getStock());
        updated.setPrice(product.getPrice());
        updated.setForSale(product.getForSale());
        updated.setPhotoUrl(product.getPhotoUrl());
        updated.setCategoryId(product.getCategoryId());

        updated = productRepository.save(updated);
        return new ResponseUtil<>(updated);
    }

    // Delete product
    public ResponseUtil<String> deleteProduct(String productId) {
        Optional<Product> existing = productRepository.findById(productId);
        if (existing.isEmpty()) {
            return new ResponseUtil<>("Product not found");
        }

        productRepository.delete(productId);
        return new ResponseUtil<>("Product deleted successfully.");
    }

    // Helper to validate product fields
    private String validateProduct(Product product) {
        if (product.getStock() == null ||product.getStock() < 0) {
            return "Stock must be provided and cannot be negative";
        }
        if (product.getPrice() == null || product.getPrice() < 0) {
            return "Price must be provided and cannot be negative";
        }
        if (product.getForSale() == null || (product.getForSale() != true && product.getForSale() != false)) { // redundant in Java, but for completeness
            return "Please define product type";
        }
        return null;
    }

}
