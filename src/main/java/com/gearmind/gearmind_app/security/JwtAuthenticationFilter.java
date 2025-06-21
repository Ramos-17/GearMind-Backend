package com.gearmind.gearmind_app.security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        
        if (token != null && tokenProvider.validateToken(token)) {
            String username = tokenProvider.getUsernameFromToken(token);
            String role = tokenProvider.getRoleFromToken(token);
            
            
            System.out.println(" JWT Authentication - Username: " + username + ", Role: " + role);
            System.out.println(" Request URL: " + request.getRequestURI());
            System.out.println(" Request Method: " + request.getMethod());
            
            
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            
            
            UsernamePasswordAuthenticationToken auth =
              new UsernamePasswordAuthenticationToken(
                  username, 
                  null, 
                  Collections.singletonList(authority)
              );
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            System.out.println(" Authentication set with authorities: " + auth.getAuthorities());
        } else if (token != null) {
            
            System.out.println(" JWT Authentication failed - Token: present, Valid: false");
        }
        
        filterChain.doFilter(request, response);
    }
}
