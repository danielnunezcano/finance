package com.nunez.finance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {

		try {
			// Autentica al usuario. El AuthenticationManager ya usa BCryptPasswordEncoder
			// internamente para comparar la contraseña con el hash almacenado en BBDD.
			final Authentication authentication = this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));

			// El principal ya contiene el UserDetails cargado durante la autenticación
			final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			final Map<String, Object> response = new HashMap<>();
			response.put("message", "Login successful");
			response.put("username", userDetails.getUsername());
			response.put("roles", userDetails.getAuthorities().stream()
					.map(Object::toString)
					.toList());

			return ResponseEntity.ok(response);

		} catch (final BadCredentialsException e) {
			final Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Invalid username or password");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		} catch (final Exception e) {
			final Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Login failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
}
