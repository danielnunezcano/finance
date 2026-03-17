package com.nunez.finance.controller;

import com.nunez.finance.model.User; // Import User model
import com.nunez.finance.service.UserDetailsServiceImpl; // Import UserDetailsService implementation
import lombok.RequiredArgsConstructor; // For constructor injection
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor // Use Lombok for dependency injection
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService; // Inject UserDetailsService if needed for other operations
    private final BCryptPasswordEncoder passwordEncoder; // Inject password encoder

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam String username,
            @RequestParam String password) {

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // If authentication is successful, Spring Security will set the authenticated user
            // For now, we'll just return a success message.
            
            // Fetch UserDetails for more info if needed (e.g., roles)
            // UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful");
            // In a real application, you would generate and return a token here.
            // response.put("token", generateToken(authentication)); 
            
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            // Handle incorrect password or username
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            // Handle other potential errors during authentication
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
