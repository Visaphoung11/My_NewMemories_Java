package com.example.mymemories.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtProvider {
     @Value("${app.jwtSecret}")
    private String jwtSecret;
     @Value("${app.jwtExpirationInMS}")
     private int jwtExpirationInMS;

     private SecretKey key(){
         return Keys.hmacShaKeyFor(jwtSecret.getBytes());
     }

     public String generateToken(String username, Long userId){
         Date now = new Date();
         Date expiration = new Date(now.getTime() + jwtExpirationInMS);
         return Jwts.builder()
                 .setSubject(username)
                 .claim("long", userId.toString())
                 .claim("username", username)
                 .setIssuedAt(now)
                 .setExpiration(expiration)
                 .signWith(key(), SignatureAlgorithm.HS256)
                 .compact();

     }

     public boolean validateToken(String token){
         try {
             Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
             return true;
         } catch (JwtException e) {
             //log
         }
         return false;
     }

     public Claims getClaims(String token){
         return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();

     }


}
