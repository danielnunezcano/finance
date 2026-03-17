package com.nunez.finance.service;

import com.nunez.finance.model.User;
import com.nunez.finance.repository.UserRepository;
import lombok.RequiredArgsConstructor; // Import RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // Add this annotation
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository; // Lombok will generate constructor for this

    // Remove @Autowired from the constructor if present, or just rely on @RequiredArgsConstructor

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
