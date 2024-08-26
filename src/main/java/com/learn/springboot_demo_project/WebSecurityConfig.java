package com.learn.springboot_demo_project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        System.out.println("\nInside securityFilterChain method");

        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/actuator/*", "/home", "/").permitAll()   // Allow actuator and home page
                                .requestMatchers("/hello").authenticated()  // Protected URL
//                        .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin((form) -> form
                        .loginPage("/login").permitAll()
                )
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailService() {
        String username = "admin";
        String password = "{noop}admin";

        System.out.println("\nCreating inmemory user, with username " + username + " and password " + password + "\n");
        UserDetails user = User.withUsername(username)
                .password(password)
                .authorities("ROLE_ADMIN")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}
