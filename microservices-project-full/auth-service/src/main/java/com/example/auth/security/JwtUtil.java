package com.example.auth.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

 private final String SECRET =
         "mysecretkeymysecretkeymysecretkeymysecretkey";

 private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

 public String generateToken(String email, String role) {

  return Jwts.builder()
          .subject(email)
          .claim("role", role)
          .issuedAt(new Date())
          .expiration(
                  new Date(System.currentTimeMillis() + 86400000))
          .signWith(key)
          .compact();
 }

 public Claims validate(String token) {

  return Jwts.parser()
          .verifyWith((javax.crypto.SecretKey) key)
          .build()
          .parseSignedClaims(token)
          .getPayload();
 }
}
