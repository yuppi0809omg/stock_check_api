package com.example.stock_check_api.repository;

import com.example.stock_check_api.entity.Item;
import com.example.stock_check_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    public Optional<Item> findById(int itemId);

    public List<Item> findByItemName(String itemName);

    public List<Item> findByItemNameAndUser(String itemName, User user);

    public List<Item> findByItemNameAndIdNot(String itemName, int itemId);

    public List<Item> findByItemNameAndUserAndIdNot(String itemName, User user, int itemId);

    public List<Item> findByItemNameContainingAndUserOrderByIdDesc(String itemName, User user);

    public List<Item> findByUserOrderByIdDesc(User user);
}
