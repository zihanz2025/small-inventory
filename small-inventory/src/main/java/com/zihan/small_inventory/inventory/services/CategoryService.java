package com.zihan.small_inventory.inventory.services;

import com.zihan.small_inventory.inventory.items.Category;
import com.zihan.small_inventory.inventory.repositories.CategoryRepository;
import com.zihan.small_inventory.utils.ResponseUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Create a new category (with name duplication check)
    public ResponseUtil<Category> createCategory(String shopId, String name) {
        if (categoryRepository.findByShopIdAndName(shopId, name)) {
            return new ResponseUtil<>("Category name already exists for this shop.");
        }

        Category category = Category.newCategory(shopId, name);
        categoryRepository.save(category);
        return new ResponseUtil<>(category);
    }

    // Get all categories for a shop
    public ResponseUtil<List<Category>> getCategoriesByShop(String shopId) {
        List<Category> categories = categoryRepository.findByShopId(shopId);
        return new ResponseUtil<>(categories);
    }

    // Get a single category
    public ResponseUtil<Category> getCategory(String shopId, String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty() || !category.get().getShopId().equals(shopId)) {
            return new ResponseUtil<>("Category not found for this shop.");
        }
        return new ResponseUtil<>(category.get());
    }

    // Update category name
    public ResponseUtil<Category> updateCategory(String shopId, String categoryId, String newName) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty() || !categoryOpt.get().getShopId().equals(shopId)) {
            return new ResponseUtil<>("Category not found for this shop.");
        }

        Category category = categoryOpt.get();

        // Check for name duplication
        if (categoryRepository.findByShopIdAndName(shopId, newName)) {
            return new ResponseUtil<>("Category name already exists for this shop.");
        }

        category.setName(newName);
        categoryRepository.save(category);
        return new ResponseUtil<>(category);
    }

    // Delete a category
    public ResponseUtil<String> deleteCategory(String shopId, String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty() || !category.get().getShopId().equals(shopId)) {
            return new ResponseUtil<>("Category not found for this shop.");
        }

        categoryRepository.delete(shopId, categoryId);
        return new ResponseUtil<>("Category deleted successfully.");
    }
}
