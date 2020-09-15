package com.example.stock_check_api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class LoginForm {


    @NotEmpty(message = "nameまたはemailの値が空です")
    @NotNull(message = "nameまたはemailは必須項目です")
    private String usernameOrEmail;

    @NotNull(message = "passwordは必須項目です")
    @NotEmpty(message = "passwordの値が空です")
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }


    public String getPassword() {
        return password;
    }


    public LoginForm() {
    }




}
