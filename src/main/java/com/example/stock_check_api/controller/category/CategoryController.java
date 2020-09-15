package com.example.stock_check_api.controller.category;

import com.example.stock_check_api.entity.Category;
import com.example.stock_check_api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/items/categories")
@Controller
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<Category>>getCategories() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);

    }

}

