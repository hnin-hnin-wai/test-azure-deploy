package edu.miu.cse.heartlink.config;

import edu.miu.cse.heartlink.constant.Permission;
import edu.miu.cse.heartlink.constant.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception {
        http.
                csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers("/api/v1/auth/*").permitAll()
                                        .requestMatchers("/api/v1/auth/authenticate").permitAll()
                                        .requestMatchers("/api/v1/categories/**").hasRole(Role.ADMIN.name())
                                        .requestMatchers("api/v1/items/**").hasAnyRole(Role.ADMIN.name(),Role.MEMBER.name())
                                        .requestMatchers("api/v1/itemclaims/**").hasAnyRole(Role.ADMIN.name(),Role.MEMBER.name())
                                        .requestMatchers("api/v1/itemtransactions/**").hasAnyRole(Role.ADMIN.name(),Role.MEMBER.name())
                                        .requestMatchers("api/v1/conversations/**").hasAnyRole(Role.ADMIN.name(),Role.MEMBER.name())
                                        .requestMatchers("api/v1/messages/**").hasAnyRole(Role.ADMIN.name(),Role.MEMBER.name())
                                        .anyRequest()
                                        .authenticated()
                ).authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http. build();
    }

}
