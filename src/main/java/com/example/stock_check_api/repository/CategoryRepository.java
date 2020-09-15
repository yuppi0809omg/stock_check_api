package com.example.stock_check_api.repository;

import com.example.stock_check_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
//    public Optional<Item> findById(int itemId);

    public List<Category> findAll();
    Optional<Category> findById(int id);

}
