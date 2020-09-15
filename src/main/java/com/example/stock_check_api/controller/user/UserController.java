package com.example.stock_check_api.controller.user;

import com.example.stock_check_api.dto.UserDto;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.response.AuthResponse;
import com.example.stock_check_api.security.UserPrincipal;
import com.example.stock_check_api.service.AuthService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins={ "http://localhost:3000", "http://192.168.1.7:3000", "http://192.168.1.4"})
@RestController
public class UserController {


    private AuthService authService;
    private ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;

    }

    @GetMapping(path = "api/users/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserDto userDto = modelMapper.map(userPrincipal, UserDto.class);
        return ResponseEntity.ok(userDto);

    }

    @DeleteMapping(path="api/user")
    public ResponseEntity<AuthResponse>deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal){
        authService.delete(userPrincipal.getId());
        return ResponseEntity.ok(new AuthResponse((Translator.toLocale("user.delete.success"))));
    }
}
