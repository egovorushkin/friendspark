package com.friendspark.backend.config

import com.friendspark.backend.security.FirebaseTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * This configuration sets up the stateless session management (critical for token-based auth),
 * disables CSRF, and injects the custom FirebaseTokenFilter to run on every request.
 */

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val firebaseTokenFilter: FirebaseTokenFilter
) {

    /**
     * 5. Minimal UserDetailsService Bean
     * * This bean is provided explicitly to satisfy Spring Security's auto-configuration,
     * which otherwise generates an InMemoryUserDetailsManager and the "Using generated
     * security password" warning. Since Firebase handles authentication, this bean
     * is not used for actual token validation.
     */
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            // Throw an exception to ensure no one accidentally relies on this for login.
            throw UsernameNotFoundException("Authentication handled by Firebase ID Token.")
        }
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // 1. Disable Cross-Site Request Forgery (CSRF)
            .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() }

            // 2. Configure Session Management as STATELESS (Crucial for token-based auth)
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

            // 3. Define authorization rules
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/error").permitAll()
                    // Permit public access to endpoints (e.g., health check)
                    .requestMatchers(
                        "/api/public/**",
                        "/actuator",
                        "/actuator/**"
                    ).permitAll()
                    // Require authentication for all other endpoints
                    .anyRequest().authenticated()
            }

            // 4. Inject the custom Firebase token validation filter
            // This filter runs before Spring's standard authentication process
            .addFilterBefore(firebaseTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}