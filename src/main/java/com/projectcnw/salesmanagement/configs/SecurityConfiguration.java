package com.projectcnw.salesmanagement.configs;

import com.projectcnw.salesmanagement.models.enums.RoleType;
import com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth.CustomerDetailService;
import com.projectcnw.salesmanagement.services.auth.CustomerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Order(1)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomerUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/admin/auth/**").permitAll()
                        .requestMatchers("/admin/shop/**").hasAuthority(RoleType.ADMIN.name())
                        .requestMatchers("/admin/staffs/**").hasAuthority(RoleType.ADMIN.name())
                        .requestMatchers("/admin/upload/**").hasAuthority(RoleType.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/admin/shop/*").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.CARE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/shop/*").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.CARE.name())

                        .requestMatchers(HttpMethod.GET, "/admin/customer/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.CARE.name(), RoleType.SALE.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/customer/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.CARE.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/customer/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.CARE.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/customer/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.CARE.name(), RoleType.SALE.name())

                        .requestMatchers(HttpMethod.GET, "/admin/base-products/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/base-products/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/base-products/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/base-products/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())

                        .requestMatchers(HttpMethod.GET, "/admin/category/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/category/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/category/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/category/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())

                        .requestMatchers(HttpMethod.POST, "/admin/balances/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.GET, "/admin/balances/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())

                        .requestMatchers(HttpMethod.GET, "/admin/import-item/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/import-item/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/import-item/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/import-item/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())

                        .requestMatchers(HttpMethod.GET, "/admin/import/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/import/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/import/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/import/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())

                        .requestMatchers(HttpMethod.GET, "/admin/vendor/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/vendor/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/vendor/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/vendor/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.WAREHOUSE.name())

                        .requestMatchers("/admin/orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name(), RoleType.WAREHOUSE.name(), RoleType.CARE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name(), RoleType.WAREHOUSE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name(), RoleType.WAREHOUSE.name())

                        .requestMatchers(HttpMethod.GET, "/admin/return_orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/return_orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/return_orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/return_orders/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())


                        .requestMatchers(HttpMethod.GET, "/admin/promotion/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.POST, "/admin/promotion/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.PUT, "/admin/promotion/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())
                        .requestMatchers(HttpMethod.DELETE, "/admin/promotion/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name())

                        .requestMatchers(HttpMethod.POST, "/admin/payment/**").hasAnyAuthority(RoleType.ADMIN.name(), RoleType.SALE.name(), RoleType.WAREHOUSE.name())
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
