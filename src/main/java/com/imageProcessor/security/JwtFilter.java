package com.imageProcessor.security;


import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.imageProcessor.service.UsuarioService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

    	String path = request.getServletPath();

    	if (path.equals("/home")) System.out.println("Intenta entrar en home");

    	// Excluir el login del filtro
    	if (path.equals("/usuarios/login") || path.equals("/login") || path.equals("/register")) {
    		filterChain.doFilter(request, response);
    	    return;
    	}

    	if (path.equals("/home")) System.out.println("Intenta entrar en home. Después del filtro de login");
    	
    	String authHeader = ""; 
    	authHeader= request.getHeader("Authorization");
    	if (authHeader == null || authHeader.equals("")) {
    		Cookie[] cookies = request.getCookies();
    		if (cookies!= null) {
    			for (Cookie cookie : cookies) {
    				if (cookie.getName().equals("token")) {
    					authHeader = "Bearer " + cookie.getValue();
    				}
    			}
    		}
    	}

    	if (path.equals("/home")) System.out.println("Intenta entrar en home. Después del filtro de login. Auth: " + authHeader);
    	
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

    	if (path.equals("/home")) System.out.println("Intenta entrar en home. Tiene Bearer");

        String token = authHeader.substring(7); // Quita "Bearer "
        try {
            String email = jwtUtil.obtenerEmailDesdeToken(token);

            if (usuarioService.buscarPorEmail(email).isPresent()) {
                // Si el usuario existe, permite la solicitud
            	UserDetails userDetails = usuarioService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());   // con roles
//                		userDetails, null, null);                             // sin roles 
                log.info("Autenticando usuario con email: {}", email);
                log.info("Authorities: {}", userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.getContext().setAuthentication(null); // No autenticamos con Spring Security aún
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se puede obtener el JWT Token");
            return;
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
