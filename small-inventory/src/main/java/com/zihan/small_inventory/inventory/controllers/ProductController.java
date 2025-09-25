package com.zihan.small_inventory.inventory.controllers;

import com.zihan.small_inventory.inventory.items.Product;
import com.zihan.small_inventory.inventory.services.ProductService;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops/{shopId}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseUtil<Product> createProduct(
            @PathVariable String shopId,
            @RequestBody Product request
    ) {
        request.setShopId(shopId);
        return productService.createProduct(request);
    }

    @GetMapping
    public ResponseUtil<List<Product>> getProductsByCategory(
            @PathVariable String shopId,
            @RequestParam String categoryId
    ) {
        return productService.getProductsByShopAndCategory(shopId, categoryId);
    }

    @GetMapping("/{productId}")
    public ResponseUtil<Product> getProductById(@PathVariable String productId) {
        return productService.getProductById(productId);
    }

    @PutMapping("/{productId}")
    public ResponseUtil<Product> updateProduct(
            @PathVariable String productId,
            @PathVariable String shopId,
            @RequestBody Product request
    ) {
        request.setProductId(productId);
        request.setShopId(shopId);
        return productService.updateProduct(request);
    }

    @DeleteMapping("/{productId}")
    public ResponseUtil<String> deleteProduct(@PathVariable String productId) {
        return productService.deleteProduct(productId);
    }
}

