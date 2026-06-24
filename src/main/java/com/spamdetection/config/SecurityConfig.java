package com.spamdetection.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * SECURITY CONFIG
 * Replaces Django's:
 *   - AUTH_PASSWORD_VALIDATORS in settings.py
 *   - login_required decorator
 *   - @login_required
 *   - CSRF middleware (Spring handles CSRF automatically for HTML forms)
 *   - urls.py auth paths
 *
 * NOTE: Spring Boot handles CSRF automatically for Thymeleaf forms!
 * Thymeleaf's th:action="@{/url}" automatically injects the CSRF token —
 * so you never get the 403 CSRF error you had in Django if you use th:action.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // ---- URL Authorization (replaces Django's login_required) ----
            .authorizeHttpRequests(auth -> auth
                // Public pages — no login needed
                .requestMatchers("/", "/home", "/register", "/login",
                                 "/css/**", "/js/**", "/images/**").permitAll()
                // Admin pages — ROLE_ADMIN only (like Django's is_staff)
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // All other pages require login
                .anyRequest().authenticated()
            )

            // ---- Login (replaces Django's login view) ----
            .formLogin(form -> form
                .loginPage("/login")              // GET /login → show login page
                .loginProcessingUrl("/login")     // POST /login → process login
                .defaultSuccessUrl("/dashboard", true)  // redirect after login
                .failureUrl("/login?error=true")  // redirect on wrong password
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )

            // ---- Logout (replaces Django's logout view) ----
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            )

            // ---- CSRF is ENABLED by default in Spring Security ----
            // Thymeleaf th:action="@{...}" auto-injects csrf token
            // No manual {% csrf_token %} needed like in Django!
            ;

        return http.build();
    }

    // BCrypt password encoder — replaces Django's PBKDF2PasswordHasher
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
