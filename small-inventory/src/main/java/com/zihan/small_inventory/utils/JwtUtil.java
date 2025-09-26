package com.zihan.small_inventory.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // Ideally store this in application.properties or environment variable
    private final String secret = "mykeythatisusedtomaketheshopfortheinventorysystem";
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes());

    // Token validity in milliseconds (e.g., 1 hour)
    private final long jwtExpirationMs = 3600_000;

    // Generate a JWT token
    public String generateToken(String shopId, String role) {
        return Jwts.builder()
                .setSubject(shopId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate the token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    // Extract shopId from token
    public String getShopIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    /**
     * Extract role from token
     */
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}
