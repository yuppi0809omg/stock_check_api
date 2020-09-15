package com.example.stock_check_api.repository;

import com.example.stock_check_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findById(Long gitHubId);
    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail, String userNameOrEmail);

}
