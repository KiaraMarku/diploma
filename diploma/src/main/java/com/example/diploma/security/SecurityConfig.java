package com.example.diploma.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    //using default tables
   @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/professor/**").hasRole("PROFESSOR")
                                .requestMatchers("/student/**").hasRole("STUDENT")
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/showLoginPage")
                                .loginProcessingUrl("/authenticateUser")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/accessDenied"))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
