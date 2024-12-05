package com.dev.projecttesting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(customizer -> customizer.disable()) // Disable CSRF for testing
                .authorizeRequests()
                .requestMatchers("/user/login", "/user/register", "/user/verify-otp",
                        "user/all-users","user/respective-user/{id}","user/contact-inquiries",
                        "user/delete/{id}","user/send-reply",
                        "/tour-details/gettours","/tour-details/respective-tour/{id}",
                        "tour-details/bookings","tour-details/bookings/{id}",
                        "/user/contact-expert","tour-details/book-tour","user/profile","user/refresh-token",
                        "/user/delete","user/update","tour-details/create","tour-details/update/{id}",
                        "/monuments/add-monument","monuments/monument/{id}","monuments/display-monuments","monuments/delete/{id}",
                        "/monuments/update/{id}",
                        "hotels/add-hotel","hotels/delete-hotel/{id}","hotels/update-hotel/{id}","hotels/display-hotels").permitAll()
                .requestMatchers("/user/profile").authenticated()// Allow these endpoints
                .anyRequest().authenticated() // Protect all other requests
                .and()
                .httpBasic(dev -> dev.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .cors(customizer -> customizer.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.addAllowedOrigin("http://localhost:9618");
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.addAllowedMethod("*");
                    return corsConfiguration;
                }));
        // Disable default form login
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:9618")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
