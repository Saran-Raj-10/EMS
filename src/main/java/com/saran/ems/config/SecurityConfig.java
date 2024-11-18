package com.saran.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername("saran")
            .password(passwordEncoder().encode("saran123"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disable CSRF for simplicity (consider enabling it in production)
            .authorizeRequests()
                .requestMatchers("/login", "/css/**", "/js/**").permitAll() // Allow login and static resources
                .anyRequest().authenticated() // Secure all other pages
                .and()
            .formLogin()
                .loginPage("/login") // Set custom login page
                .loginProcessingUrl("/login") // URL to submit login form
                .defaultSuccessUrl("/employees", true) // Redirect to employees page after login
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout") // Logout URL
                .logoutSuccessUrl("/login?logout") // Redirect to login page after logout
                .permitAll();

        return http.build();
    }

}
