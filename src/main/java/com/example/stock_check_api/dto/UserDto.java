package com.example.stock_check_api.dto;

public class UserDto {

    private int id;
    private String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserDto(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public UserDto() {
    }
}
