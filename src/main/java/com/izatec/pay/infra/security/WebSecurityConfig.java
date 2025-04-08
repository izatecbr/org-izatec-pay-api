package com.izatec.pay.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Autowired
    private JwtFilter filter;

    private static final String[] SWAGGER_WHITELIST = {
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST, "/acessos/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pagamentos/compensacao").permitAll()
                        .requestMatchers(HttpMethod.POST, "/pagamentos/compensacao/lytex").permitAll()
                        .requestMatchers("/publico/**").permitAll()
                        .anyRequest().authenticated()
                ).addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public RequisicaoInfo requestInfo() {
        return new RequisicaoInfo();
    }
}