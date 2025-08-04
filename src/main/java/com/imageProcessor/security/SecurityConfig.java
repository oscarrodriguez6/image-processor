package com.imageProcessor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
	        .cors(cors -> cors.configurationSource(request -> {
	            CorsConfiguration config = new CorsConfiguration();
	            config.setAllowedOrigins(List.of("http://localhost:5173")); // Cambia por tu frontend
	            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	            config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
	            config.setAllowCredentials(true);
	            return config;
	        }))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/login", "/register", "/error").permitAll() // Permitir todas estas rutas sin autenticación
                .requestMatchers(HttpMethod.POST, "/login", "/register", "/error").permitAll() // Permitir todas estas rutas sin autenticación
                .requestMatchers("/usuarios/login").permitAll() // Permitir la API de login
                .requestMatchers("/usuarios/registro").permitAll() // Permitir la API de login
                .requestMatchers("/uploads/miniaturas/**").permitAll() // Temporal para separación de aplicaciones viajes / fotos
                // Permitir archivos estáticos de viajes (html, js, css, etc.)
                .requestMatchers("/viajes/*.html", "/viajes/*.js", "/viajes/*.css", "/viajes/*.ico", "/viajes/assets/**").permitAll()
                // Si usas rutas como /viajes/static/** o /static/**, añádelas también
                .requestMatchers("/static/**").permitAll()
                // El resto de /viajes/** sigue protegido
                .requestMatchers("/viajes/**").authenticated()
                 .anyRequest().authenticated() // Proteger cualquier otra ruta
            )
            .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
            })) // Maneja errores 403 correctamente
            .formLogin(form -> form.disable()) // Deshabilita el login automático de Spring
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//           .headers(headers -> headers
//                    .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Opener-Policy", "same-origin"))
//                    .addHeaderWriter(new StaticHeadersWriter("Cross-Origin-Embedder-Policy", "require-corp"))
//                );        
;
	    return http.build();
    }
        
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder); // No llames al método passwordEncoder()
        return authProvider;
    }

}
