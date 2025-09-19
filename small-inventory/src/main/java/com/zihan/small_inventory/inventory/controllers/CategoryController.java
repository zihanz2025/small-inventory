package com.zihan.small_inventory.inventory.controllers;

import com.zihan.small_inventory.inventory.items.Category;
import com.zihan.small_inventory.inventory.services.CategoryService;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/shops/{shopId}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create a new category
    @PostMapping
    public ResponseUtil<Category> createCategory(
            @PathVariable String shopId,
            @RequestParam String name
    ) {
        return categoryService.createCategory(shopId, name);
    }

    // List all categories for a shop
    @GetMapping
    public ResponseUtil<List<Category>> getAllCategories(@PathVariable String shopId) {
        return categoryService.getCategoriesByShop(shopId);
    }

    // Get a single category
    @GetMapping("/{categoryId}")
    public ResponseUtil<Category> getCategory(
            @PathVariable String shopId,
            @PathVariable String categoryId
    ) {
        return categoryService.getCategory(shopId, categoryId);
    }

    // Update category name
    @PutMapping("/{categoryId}")
    public ResponseUtil<Category> updateCategory(
            @PathVariable String shopId,
            @PathVariable String categoryId,
            @RequestParam String name
    ) {
        return categoryService.updateCategory(shopId, categoryId, name);
    }

    // Delete a category
    @DeleteMapping("/{categoryId}")
    public ResponseUtil<String> deleteCategory(
            @PathVariable String shopId,
            @PathVariable String categoryId
    ) {
        return categoryService.deleteCategory(shopId, categoryId);
    }
}
