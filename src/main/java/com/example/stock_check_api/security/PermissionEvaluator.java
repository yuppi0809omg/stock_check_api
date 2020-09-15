package com.example.stock_check_api.security;
import com.example.stock_check_api.service.AuthService;
import com.example.stock_check_api.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value="permissionEvaluator")
public class PermissionEvaluator {

    @Autowired
    private AuthService authService;
    @Autowired
    private ItemService itemService;


    public boolean isAuthorized(int itemId, UserPrincipal userPrincipal) {
        return itemService.findById(itemId).getUser() == authService.findById(userPrincipal.getId());
    }
}
