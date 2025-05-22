package org.example.Security;

import org.example.Security.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private JWTGenerator tokenGenerator;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Starting JWT authentication filter..."); // Log the start of the filter
        String token = getJWTFromRequest(request);
        System.out.println("Extracted token: " + token); // Existing log statement
        if(StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            System.out.println("Valid token found."); // Log when a valid token is found
            String username = tokenGenerator.getUsernameFromJWT(token);
            System.out.println("Authenticated user: " + username); // Existing log statement
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("Authentication successful."); // Log successful authentication
        } else {
            System.out.println("Invalid or missing token."); // Existing log statement
        }
        System.out.println("Completing JWT authentication filter..."); // Log the completion of the filter
        filterChain.doFilter(request, response);
    }

    private String getJWTFromRequest(HttpServletRequest request)
    {
        String bearerToken = request.getHeader("Authorization");
        System.out.println(bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7, bearerToken.length());
        else
            return null;
    }
}
