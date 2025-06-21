package com.gearmind.gearmind_app.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // If you want to use a static secret (Base64-encoded), you can inject it here
    // @Value("${app.jwtSecret}")
    // private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Use a consistent secret key for JWT signing
    private SecretKey key;

    @PostConstruct
    public void init() {
        // Use a consistent secret key (Base64 encoded)
        String secret = "your-256-bit-secret-key-here-make-it-long-enough-for-hs512";
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateToken(Authentication authentication) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String role = user.getAuthorities()
                          .stream()
                          .map(GrantedAuthority::getAuthority)
                          .findFirst()
                          .orElse("ROLE_USER");
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                   .setSubject(user.getUsername())
                   // ← Add this:
                   .claim("role", role)
                   .setIssuedAt(now)
                   .setExpiration(expiry)
                   .signWith(key)
                   .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // Invalid token
        }
        return false;
    }
}
