package edu.miu.cse.heartlink.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            //Extract the token
            String token = authorizationHeader.substring(7);

            //Extract username from the token

            Claims claims=jwtService.getClaims(token);//getting username
            String username = claims.getSubject();//finding subject
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                if (jwtService.isTokenValid(token, userDetailsService.loadUserByUsername(username))) {
                    // Set the authentication in the context
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(username), null,userDetailsService.loadUserByUsername(username).getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

  }


        }
        filterChain.doFilter(request, response);

    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/api/v1/auth");

    }
}
