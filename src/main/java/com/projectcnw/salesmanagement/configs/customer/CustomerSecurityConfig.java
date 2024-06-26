package com.projectcnw.salesmanagement.configs.customer;

import com.projectcnw.salesmanagement.models.enums.RoleType;
import com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth.CustomerDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(2)
public class CustomerSecurityConfig {

    private final CustomerJwtAuthFilter customerJwtAuthFilter;
    private final CustomerDetailService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationProvider authenticationProvider2() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityCustomerFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider2());
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/customer/auth/**").permitAll()
                        .requestMatchers("/api/variants/**").permitAll()
                        .requestMatchers("/api/products/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "api/user/cart/**").hasAnyAuthority(RoleType.CUSTOMER.name())
                        .requestMatchers(HttpMethod.GET, "api/user/cart/**").hasAnyAuthority(RoleType.CUSTOMER.name())
                        .requestMatchers(HttpMethod.PUT, "api/user/cart/**").hasAnyAuthority(RoleType.CUSTOMER.name())
                        .requestMatchers(HttpMethod.DELETE, "api/user/cart/**").hasAnyAuthority(RoleType.CUSTOMER.name())

                        .requestMatchers(HttpMethod.GET, "/api/customer/me").hasAnyAuthority(RoleType.CUSTOMER.name())
                        .requestMatchers(HttpMethod.PUT, "/api/customer/update").hasAnyAuthority(RoleType.CUSTOMER.name())
                        .requestMatchers(HttpMethod.PUT, "/api/customer/update-password").hasAnyAuthority(RoleType.CUSTOMER.name())

                        .requestMatchers("/api/address").hasAnyAuthority(RoleType.CUSTOMER.name())
                        .requestMatchers("/api/address/**").hasAnyAuthority(RoleType.CUSTOMER.name())

                        .requestMatchers("/api/promotion/**").hasAnyAuthority(RoleType.CUSTOMER.name())

                        .requestMatchers("/api/order/**").hasAnyAuthority(RoleType.CUSTOMER.name())
                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customerJwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
