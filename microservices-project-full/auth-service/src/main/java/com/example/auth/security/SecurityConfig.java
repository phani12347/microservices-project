
package com.example.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

 @Bean
 SecurityFilterChain filter(HttpSecurity http) throws Exception{

  http.csrf().disable()
    .authorizeHttpRequests()
    .anyRequest().permitAll();

  return http.build();
 }

 @Bean
 PasswordEncoder encoder(){
  return new BCryptPasswordEncoder();
 }
}
