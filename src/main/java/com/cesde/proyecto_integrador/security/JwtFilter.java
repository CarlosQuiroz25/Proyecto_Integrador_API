package com.cesde.proyecto_integrador.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cesde.proyecto_integrador.model.User;
import com.cesde.proyecto_integrador.repository.UserRepository;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(@SuppressWarnings("null") HttpServletRequest request, @SuppressWarnings("null") HttpServletResponse response, @SuppressWarnings("null") FilterChain filterChain)
            throws ServletException, IOException {
        
        // Permitir siempre el acceso al endpoint de respuestas sin verificar el token
        String requestURI = request.getRequestURI();
        log.debug("Request URI: {}", requestURI);
        
        if (requestURI.startsWith("/api/v1/answers")) {
            log.debug("Permitiendo acceso al endpoint de respuestas sin verificar token");
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        log.debug("Auth header received: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.debug("Processing token: {}", token);
            
            try {
                if (jwtUtil.isTokenValid(token)) {
                    String username = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractRole(token);
                    log.debug("Token valid for user: {} with role: {}", username, role);
                    
                    User user = userRepository.findByEmail(username).orElse(null);
                    if (user != null) {
                        log.debug("User found in database with role: {}", user.getRole());
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                user, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.debug("Authentication set in SecurityContext");
                    } else {
                        log.warn("User not found in database: {}", username);
                    }
                } else {
                    log.warn("Invalid token");
                }
            } catch (Exception e) {
                log.error("Error processing token: {}", e.getMessage());
            }
        } else {
            log.debug("No auth header or invalid format");
        }
        
        filterChain.doFilter(request, response);
    }
}
