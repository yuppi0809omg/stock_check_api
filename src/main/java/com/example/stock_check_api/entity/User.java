package com.example.stock_check_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(name="password", nullable = false)
    private String password;

    @JsonIgnore
    @Column(name="email", nullable = false,  unique = true)
    private String email;



    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE})
    private List<Item> items;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public User() {
    }

//    public List<Item> getItems() {
//        return items;
//    }
//
//    public void setItems(List<Item> items) {
//        this.items = items;
//    }



}


