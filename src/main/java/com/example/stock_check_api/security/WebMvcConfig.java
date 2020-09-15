//package com.example.stock_check_api.security;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer  {
//
//    @Value("${react-stock-check-app-url}")
//    private String REACT_STCK_CEHCK_APP_URL;
//
//        @Bean
//        public WebMvcConfigurer corsConfigurer() {
//            return new WebMvcConfigurer() {
//                @Override
//                public void addCorsMappings(CorsRegistry registry) {
//                    registry.addMapping("/**").allowedOrigins("http://localhost:3000/**", "http://192.168.1.7:3000/**", "http://192.168.1.4/**")
//                    .allowedMethods("OPTIONS", "GET", "PUT", "DELETE", "POST");
//                }
//            };
//        }
//}
////
////package com.example.stock_check_api.interceptor;
////
////        import java.util.Arrays;
////
////        import org.springframework.beans.factory.annotation.Autowired;
////        import org.springframework.context.annotation.Bean;
////        import org.springframework.context.annotation.Configuration;
////        import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
////        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
////
////        import com.example.stock_check_api.service.UserService;
////
////@Configuration
////public class WebMvcConfig implements WebMvcConfigurer {
////
////    @Autowired
////    UserService userService;
////
////    @Bean
////    public OAuthInterceptor oAuthInterceptor() {
////        return new OAuthInterceptor(userService);
////    }
////
////    @Override
////    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(oAuthInterceptor()).addPathPatterns(Arrays.asList("/api/items/**", "/api/items"));
////    }
////}
