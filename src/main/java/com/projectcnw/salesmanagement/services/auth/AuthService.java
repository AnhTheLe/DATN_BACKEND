package com.projectcnw.salesmanagement.services.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectcnw.salesmanagement.dto.auth.AuthDto;
import com.projectcnw.salesmanagement.dto.auth.AuthResponse;
import com.projectcnw.salesmanagement.dto.auth.UserInfoDto;
import com.projectcnw.salesmanagement.dto.auth.VerifyTokenRequest;
import com.projectcnw.salesmanagement.exceptions.NotFoundException;
import com.projectcnw.salesmanagement.exceptions.UnAuthorizedException;
import com.projectcnw.salesmanagement.models.Auth.Token;
import com.projectcnw.salesmanagement.models.Auth.UserEntity;
import com.projectcnw.salesmanagement.models.enums.TokenType;
import com.projectcnw.salesmanagement.repositories.TokenRepository;
import com.projectcnw.salesmanagement.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TokenRepository tokenRepository;


    public AuthResponse authenticate(AuthDto authDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDto.getPhone(),
                        authDto.getPassword()
                )
        );
        UserEntity userEntity = userRepository.findByPhone(authDto.getPhone())
                .orElseThrow(() -> new NotFoundException("profile not found"));

        var jwtToken = jwtService.generateToken(userEntity);
        var refreshToken = jwtService.generateRefreshToken(userEntity);
        revokeAllUserTokens(userEntity);
        saveUserToken(userEntity, jwtToken);
        ModelMapper modelMapper = new ModelMapper();
        return AuthResponse.builder()
                .user(modelMapper.map(userEntity, UserInfoDto.class))
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponse register(AuthDto authDto) {
        boolean userEntity = userRepository.findByPhone(authDto.getPhone())
                .isPresent();
        UserEntity userEntity1 = new UserEntity();
        if (!userEntity) {
            userEntity1.setPassword(passwordEncoder.encode(authDto.getPassword()));
            userRepository.save(userEntity1);
            String jwtToken = jwtService.generateToken(userEntity1);
            String refreshToken = jwtService.generateRefreshToken(userEntity1);
            saveUserToken(userEntity1, jwtToken);
            ModelMapper modelMapper = new ModelMapper();
            return AuthResponse.builder()
                    .user(modelMapper.map(userEntity1, UserInfoDto.class))
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }

    public UserInfoDto verifyToken(VerifyTokenRequest request) {
        String phone = jwtService.extractUsername(request.getToken());
        UserEntity userEntity = userRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("profile not found"));
        if (!jwtService.isTokenValid(request.getToken(), userEntity)) {
            throw new UnAuthorizedException();
        }

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userEntity, UserInfoDto.class);
    }

    private void saveUserToken(UserEntity user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(Long.valueOf(user.getId()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthResponse refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userPhone;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        userPhone = jwtService.extractUsername(refreshToken);
        if (userPhone != null) {
            var user = this.userRepository.findByPhone(userPhone)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                ModelMapper modelMapper = new ModelMapper();
                var authResponse = AuthResponse.builder()
                        .user(modelMapper.map(user, UserInfoDto.class))
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
        return null;
    }
}