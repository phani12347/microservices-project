
package com.example.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.security.JwtUtil;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/auth")
public class AuthController {

 @Autowired
 private UserRepository repo;

 @Autowired
 private JwtUtil jwt;

 @Autowired
 private PasswordEncoder encoder;

 @PostMapping("/register")
 public ResponseEntity<?> register(@RequestBody User user){

  if(user.getEmail()==null || user.getPassword()==null){
   return ResponseEntity.badRequest().body("Missing fields");
  }

  if(repo.findByEmail(user.getEmail()).isPresent()){
   return ResponseEntity.badRequest().body("User already exists");
  }

  user.setPassword(encoder.encode(user.getPassword()));
  user.setRole("USER");

  return ResponseEntity.ok(repo.save(user));
 }


 @PostMapping("/login")
 public Map<String,String> login(@RequestBody Map<String,String> data){

  User user = repo.findByEmail(data.get("email")).orElseThrow();

  if(!encoder.matches(data.get("password"), user.getPassword())){
   throw new RuntimeException("Invalid Credentials");
  }

  String token = jwt.generateToken(user.getEmail(), user.getRole());

  return Map.of("token", token);
 }

 @GetMapping("/validate-token")
 public Claims validate(@RequestHeader("Authorization") String token){

  return jwt.validate(token.replace("Bearer ",""));
 }
}
