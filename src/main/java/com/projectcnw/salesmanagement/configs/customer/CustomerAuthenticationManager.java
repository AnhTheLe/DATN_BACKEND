package com.projectcnw.salesmanagement.configs.customer;

import com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth.CustomerDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;


@Configuration
@EnableWebSecurity
public class CustomerAuthenticationManager extends DaoAuthenticationProvider {

    private final CustomerDetailService userDetailsService;
    private final PasswordEncoder encoder;

    public CustomerAuthenticationManager(CustomerDetailService userDetailsService, PasswordEncoder encoder) {
        this.userDetailsService = userDetailsService;
        this.encoder = encoder;
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws BadCredentialsException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        // Check if username and password are not empty
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BadCredentialsException("Invalid phone or password");
        }

        // Lấy thông tin UserDetails từ CustomerUserDetailsService
        UserDetails user = userDetailsService.loadUserByUsername(username);

        // So sánh mật khẩu
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid phone or password");
        }

        // Tạo Authentication thành công
        return createSuccessAuthentication(authentication, user);
    }

    private Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
        // Tạo Authentication thành công
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }

}

