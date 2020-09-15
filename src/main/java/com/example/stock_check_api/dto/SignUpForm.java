package com.example.stock_check_api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignUpForm {

    private int id;

    @NotEmpty(message = "nameの値が空です")
    @NotNull(message = "nameは必須項目です")
    @Size(max = 100, message = "usernameは100文字以下でご入力ください")
    private String username;

    @NotNull(message = "emailは必須項目です")
    @NotEmpty(message = "emailの値が空です")
    private String email;

    @NotNull(message = "passwordは必須項目です")
    @NotEmpty(message = "passwordの値が空です")
    private String password;

    public String getEmail() {
        return email;
    }

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

    public String getPassword() {
        return password;
    }


    public SignUpForm() {
    }




}
