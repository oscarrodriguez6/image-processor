package com.webapp.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

@Controller
@RequestMapping("/")
public class AuthController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "http://localhost:8080/usuarios";

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model, HttpServletResponse response) {
        try {
            System.out.println("Traza Oscar antes de llamada");

            ResponseEntity<Map> responseAuth = restTemplate.postForEntity(
                    API_URL + "/login?email=" + email + "&password=" + password, null, Map.class);
            System.out.println("Traza Oscar: " + responseAuth.getStatusCode());
            System.out.println("Traza Oscar: " + responseAuth.getStatusCode().is2xxSuccessful());
            System.out.println("Traza Oscar: " + responseAuth.getBody().get("token"));
            if (responseAuth.getStatusCode().is2xxSuccessful()) {
            	System.out.println("Entra en el if");
                model.addAttribute("token", responseAuth.getBody().get("token"));
                System.out.println("despues del model");
                String token = responseAuth.getBody().get("token").toString();
                System.out.println("Token para la cookie: " + token);
                Cookie cookie = new Cookie("token", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setAttribute("SameSite", "Lax");
                cookie.setPath("/");
                cookie.setMaxAge(3600);
                System.out.println("Antes del response: " + cookie.toString());
                try {
                	response.addCookie(cookie);
                	System.out.println("Redirect home");
                } catch (Exception e) {
                	System.out.println("Error al agregar la cookie: " + e.getMessage());
                    e.printStackTrace();
				}
                return "redirect:/home";
            } else {
                model.addAttribute("error", "Usuario o contraseña incorrectos");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error en la autenticación");
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String email, @RequestParam String password, Model model) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    API_URL + "/registro?nombre=" + "pepe" + "&email=" + email + "&password=" + password, null, String.class);
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return "redirect:/login";
            } else {
                model.addAttribute("error", "Error al registrar usuario");
                return "register";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error en el registro");
            return "register";
        }
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
