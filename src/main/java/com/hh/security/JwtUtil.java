/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.security;

//import io.jsonwebtoken.JwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import javax.crypto.SecretKey;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;

/**
 *
 * @author Javier
 */
//@NoArgsConstructor
//@AllArgsConstructor
//@Component
public class JwtUtil {

//    @Value("${jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${jwt.expiration}")
//    private int jwtExpirationInMs;
//
//    private final SecretKey secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode("House+Hub+Implementation+V+1+Javier+0123456789+ABCDEFGH+"));
//
//    public String generateToken(String email) {
//        Map<String, Object> claims = new HashMap<>();
//        return Jwts.builder().setClaims(claims)
//                .setSubject(email)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) //jwtExpirationInMs
//                .signWith(secret, SignatureAlgorithm.HS512)
//                .compact();
//    }
//
////    public Boolean validateToken(String token, LoginRequest user) {
////        final String username = getEmailFromToken(token);
////        return (username.equals(user.getEmail()) && !isTokenExpired(token));
////    }
//    public String getEmailFromToken(String token) {
//        return Jwts.parserBuilder().setSigningKey(secret).build()
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public Boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(secret).build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
}
