package com.nunez.finance.config;

import com.nunez.finance.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor; // Import RequiredArgsConstructor
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService; // Use interface for injection
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Add this annotation
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService; // Lombok will generate constructor for this

    // Lombok will generate the constructor for 'userDetailsService' automatically.
    // Remove the explicit constructor if it was autowired.

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManagerBuilder needs to be built within a Bean.
    // The 'authenticationManager' bean already does this properly.
    // The UserDetailsService injection here is correct.
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless API, or configure properly
            .authorizeHttpRequests(authorize -> authorize
                // Public endpoints (e.g., login, registration - not implemented yet)
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll()
                // Protected endpoints
                .requestMatchers("/api/transactions/**", "/api/users/**").authenticated()
                // Allow access to H2 console for development/testing if needed
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated() // All other requests require authentication
            )
            // Basic Authentication for simplicity, can be replaced with JWT or form login
            .httpBasic(httpBasic -> {
                // Configure basic auth here if preferred, or remove if using only JWT/form login
            })
            // Form Login for manual login - useful for browser access
            .formLogin(formLogin -> formLogin
                .loginPage("/login") // Custom login page, not implemented yet
                .permitAll()
            )
            // Add custom authentication filter here if using JWT or other methods
            // .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            ;
        return http.build();
    }
    
    // Optional: Bean for WebSecurityCustomizer to ignore certain paths from security checks (e.g., static resources)
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/v3/api-docs/**", "/configuration/ui", "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**", "/webjars/**", "/");
    }
}
