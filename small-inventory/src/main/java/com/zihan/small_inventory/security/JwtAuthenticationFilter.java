package com.zihan.small_inventory.security;

import com.zihan.small_inventory.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        System.out.println(">>> Incoming Authorization header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println(">>> Extracted token: " + token);

            if (jwtUtil.validateToken(token)) {
                String shopId = jwtUtil.getShopIdFromToken(token);
                String role = jwtUtil.getRoleFromToken(token); // extend JwtUtil to store roles
                System.out.println(">>> Token valid for shopId=" + shopId + ", role=" + role);

                ShopDetails shopDetails = new ShopDetails(
                        shopId,
                        "", // password not needed here
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                );


                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(shopDetails, null, shopDetails.getAuthorities());
                System.out.println(">>> Authentication set: " + auth);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
