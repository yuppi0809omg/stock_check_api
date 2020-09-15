package com.example.stock_check_api.controller.authetication;

import com.example.stock_check_api.dto.LoginForm;
import com.example.stock_check_api.dto.SignUpForm;
import com.example.stock_check_api.localization.Translator;
import com.example.stock_check_api.response.AuthResponse;
import com.example.stock_check_api.security.JwtTokenProvider;
import com.example.stock_check_api.service.AuthService;
import com.example.stock_check_api.utility.CustomMailSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@CrossOrigin(origins={ "http://localhost:3000", "http://192.168.1.7:3000", "http://192.168.1.4"})
@Transactional
@RestController
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final String TOKEN_TYPE = "Bearer ";
    private AuthenticationManager authenticationManager;
    private AuthService authService;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AuthService authService, JwtTokenProvider tokenProvider, CustomMailSender mailSender) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.tokenProvider = tokenProvider;
    }



    @PostMapping(path = "/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Validated @RequestBody LoginForm loginForm) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginForm.getUsernameOrEmail(),
                        loginForm.getPassword()
                )
        );

        //  The SecurityContext and SecurityContextHolder are two fundamental classes of Spring Security.
        //  The SecurityContext is used to store the details of the currently authenticated user, also known as a principle. So, if you have to get the username or any other user details, you need to get this SecurityContext first.
        //  The SecurityContextHolder is a helper class, which provides access to the security context.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new AuthResponse(TOKEN_TYPE + jwt));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Validated SignUpForm signUpForm){

        authService.signupUser(signUpForm);


        return ResponseEntity.ok(new AuthResponse(Translator.toLocale("user.signup.success")));
    }


}
