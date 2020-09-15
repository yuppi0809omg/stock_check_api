package com.example.stock_check_api.security;

import com.example.stock_check_api.filter.JwtAuthenticationFilter;
import com.example.stock_check_api.handler.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
/*@Order(2)*/
public class SpringSecurityConfigurationBasicAuth extends WebSecurityConfigurerAdapter {


    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private CustomUserDetailsService customUserDetailsService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SpringSecurityConfigurationBasicAuth(JwtAuthenticationEntryPoint unauthorizedHandler,
                                                CustomUserDetailsService customUserDetailsService,
                                                JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter();
//    }

    // Authentication Manager Bean登録。これしないとAuth Controllerで@Autowiredしてるので怒られる
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
//                    .exceptionHandling()
//                    .authenticationEntryPoint(unauthorizedHandler)
//                .and()
                .cors()
                .and()
                    .authorizeRequests()
                        .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                        .antMatchers("/authenticate", "/signup").permitAll()
                        .anyRequest().authenticated()
//                        .antMatchers("/api/items/**", "/api/items").authenticated()
                    .and()
                        .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .httpBasic().disable();//

        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                    .exceptionHandling()
//                    .authenticationEntryPoint(unauthorizedHandler)
//                .and()
//                // どのパスとどのメソッドを許可し、どのパスを認証必須にするかを設定する
//                .authorizeRequests()
////                    .antMatchers("/signup", "/authenticate").permitAll()
//                     .antMatchers("/api/items/**", "/api/items").authenticated()
////                    .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
//                .and()
////                .formLogin().and()
//                .httpBasic().disable();


    }
}
