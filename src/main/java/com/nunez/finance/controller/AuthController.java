package com.nunez.finance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {

        // TODO: Implement actual authentication logic here using UserDetailsService and AuthenticationManager
        // For now, we simulate a successful login.
        
        // In a real scenario, you'd validate credentials and return a token (e.g., JWT).
        // For this example, we'll just return a success message.
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful (simulated)");
        // You would typically return a token or user details here.
        // response.put("token", "your_jwt_token_here"); 
        
        return ResponseEntity.ok(response);
    }
}
