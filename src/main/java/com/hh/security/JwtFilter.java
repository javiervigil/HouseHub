/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.security;

//import com.hh.controller.GeneralController;
//import com.hh.data.LoginData;
//import com.hh.data.LoginRequest;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

//
//@Component
//public class JwtFilter extends OncePerRequestFilter {
public class JwtFilter {
//    @Autowired
//    private JwtUtil jwtUtil;
//    
//    @Autowired
//    private LoginService loginService;

   // @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//
//            if (!jwtUtil.validateToken(token)) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
//                return;
//            }
//
//            // Aquí podrías establecer el usuario autenticado en el contexto de Spring Security si lo deseas
//        }
//
//        filterChain.doFilter(request, response);
//    }
}