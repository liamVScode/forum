package com.example.foruminforexchange.configuration;

import com.example.foruminforexchange.model.Role;
import com.example.foruminforexchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request.requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/api/posts/detail-post").permitAll()
                        .requestMatchers("/api/posts/topic-prefix").permitAll()
                        .requestMatchers("/api/posts/all-post").permitAll()
                        .requestMatchers("/api/posts/get-all").permitAll()
                        .requestMatchers("/api/posts/filter-post").permitAll()
                        .requestMatchers("/api/posts/comments/comment-by-post").permitAll()
                        .requestMatchers("/api/posts/comments/likes/like-by-comment").permitAll()
                        .requestMatchers("/api/posts/**").authenticated()
                        .requestMatchers("/api/forum/**").permitAll()
                        .requestMatchers("/api/statistics/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ADMIN.name())
                        .requestMatchers("/api/v1/user").authenticated()
                        .requestMatchers("/api/v1/auth/logout").authenticated()
                        .requestMatchers("/api/v1/user/update-status").permitAll()
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/websocket/**").permitAll()
                        .requestMatchers("/api/chat").authenticated()
                        .requestMatchers("/Image/**").permitAll()
                        .requestMatchers("/api/v1/user/information-user", "/api/v1/user/all-activity-by-user", "/test-redis").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
