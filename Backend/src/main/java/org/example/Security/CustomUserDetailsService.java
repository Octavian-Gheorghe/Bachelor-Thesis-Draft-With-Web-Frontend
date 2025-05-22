package org.example.Security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final String hardcodedUsername = "admin";
    private final String hardcodedPassword = new BCryptPasswordEncoder().encode("admin");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!hardcodedUsername.equals(username)) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(hardcodedUsername, hardcodedPassword, Collections.emptyList());
    }
}