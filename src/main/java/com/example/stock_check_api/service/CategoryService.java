package com.example.stock_check_api.service;

import com.example.stock_check_api.entity.Category;
import com.example.stock_check_api.exception.NotFoundException;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Category findById(int id){
        return categoryRepository.findById(id).orElseThrow(()->new NotFoundException(Translator.toLocale("category.error.notfound") +id));
    }
}

