package com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectcnw.salesmanagement.configs.customer.CustomerAuthenticationManager;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.auth.AuthResponse;
import com.projectcnw.salesmanagement.dto.auth.UserInfoDto;
import com.projectcnw.salesmanagement.dto.auth.VerifyTokenRequest;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerAuthDto;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerLoginDto;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerUpdatePasswordRequest;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerUpdateRequest;
import com.projectcnw.salesmanagement.exceptions.BadRequestException;
import com.projectcnw.salesmanagement.exceptions.NotFoundException;
import com.projectcnw.salesmanagement.models.Auth.Token;
import com.projectcnw.salesmanagement.models.Customer;
import com.projectcnw.salesmanagement.models.enums.TokenType;
import com.projectcnw.salesmanagement.repositories.CustomerRepositories.CustomerRepository;
import com.projectcnw.salesmanagement.repositories.TokenRepository;
import com.projectcnw.salesmanagement.services.auth.JwtService;
import com.projectcnw.salesmanagement.utils.UserUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerAuthService {

    private final CustomerRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomerAuthenticationManager customerAuthenticationManager;

    private final CustomerDetailService userDetailsService;

    public AuthResponse authenticate(CustomerLoginDto authDto) {
        if(authDto.getEmail() == null || authDto.getPassword() == null) {
            throw new BadRequestException("Email or password is null");
        }

        customerAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDto.getEmail(),
                        authDto.getPassword()
                )
        );
        Customer userEntity = userRepository.findByEmail(authDto.getEmail()).orElseThrow(() -> new NotFoundException("profile not found"));
        String token = jwtService.generateToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        revokeAllUserTokens(userEntity);
        saveCustomerToken(userEntity, token);
        ModelMapper modelMapper = new ModelMapper();
        return AuthResponse.builder()
                .user(modelMapper.map(userEntity, UserInfoDto.class))
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public ResponseEntity<ResponseObject> getCustomerInfo(String accessToken) {
        String email = jwtService.extractUsername(accessToken);
        Optional<Customer> customer =  userRepository.findByEmail(email);
        if(customer.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Không tìm thấy thông tin khách hàng")
                    .data(null)
                    .build());
        }
        var validUserTokens = tokenRepository.findAllValidTokenByCustomer(Long.valueOf(customer.get().getId()));
        if (validUserTokens.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(401)
                    .message("Token không hợp lệ")
                    .data(null)
                    .build());
        }
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(customer.get())
                .build());
    }


    // Trường hợp khách hàng tạo tài khoản trên web thì nên tra sdt rồi bắt đầu update mật khẩu
    public ResponseEntity<ResponseObject> register(CustomerAuthDto authDto) {
        ModelMapper modelMapper = new ModelMapper();
        // Kiểm tra xem khách hàng đã tồn tại hay không
        Optional<Customer> existingCustomer = userRepository.findByPhone(authDto.getPhone());
        if (existingCustomer.isPresent()) {
            if (existingCustomer.get().getPassword() != null) {
                // Nếu đã có password trong DB, bắn ra thông báo lỗi
                return ResponseEntity.ok(ResponseObject.builder()
                        .responseCode(502)
                        .message("Tài khoản đã tồn tại")
                        .data(null)
                        .build());
            } else {
                // Nếu không có password trong DB, cập nhật password mới
                existingCustomer.get().setPassword(passwordEncoder.encode(authDto.getPassword()));
                userRepository.save(existingCustomer.get());
                var jwtToken = jwtService.generateToken(existingCustomer.get());
                String refreshToken = jwtService.generateRefreshToken(existingCustomer.get());
                saveCustomerToken(existingCustomer.get(), jwtToken);
                UserInfoDto userInfoDto = modelMapper.map(existingCustomer, UserInfoDto.class);
                userInfoDto.setPhone(existingCustomer.get().getPhone());
                userInfoDto.setFullName(existingCustomer.get().getName());
                userInfoDto.setRoles(null);
                return ResponseEntity.ok(ResponseObject.builder()
                        .responseCode(200)
                        .message("Success")
                        .data(AuthResponse.builder()
                                .user(userInfoDto)
                                .accessToken(jwtToken)
                                .refreshToken(refreshToken)
                                .build())
                        .build());
            }
        } else {
            // Nếu khách hàng chưa tồn tại, tạo mới
            var userEntity1 = new Customer();
            userEntity1.setPassword(passwordEncoder.encode(authDto.getPassword()));
            userEntity1.setPhone(authDto.getPhone());
            userEntity1.setEmail(authDto.getEmail());
            userEntity1.setName(authDto.getFirstName() + " " + authDto.getLastName());
            userRepository.save(userEntity1);

            var jwtToken = jwtService.generateToken(userEntity1);
            UserInfoDto userInfoDto = modelMapper.map(userEntity1, UserInfoDto.class);
            userInfoDto.setPhone(userEntity1.getPhone());
            userInfoDto.setFullName(userEntity1.getName());
            userInfoDto.setRoles(null);
            return ResponseEntity.ok(ResponseObject.builder()
                    .responseCode(200)
                    .message("Success")
                    .data(AuthResponse.builder()
                            .user(userInfoDto)
                            .accessToken(jwtToken)
                            .build())
                    .build());
        }
    }

    public ResponseEntity<ResponseObject> verifyToken(VerifyTokenRequest request) {
        var email = jwtService.extractUsername(request.getToken());

        Optional<Customer> customer =  userRepository.findByEmail(email);
        if(customer.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Không tìm thấy thông tin khách hàng")
                    .data(null)
                    .build());
        }

        var validUserTokens = tokenRepository.findAllValidTokenByCustomer(Long.valueOf(customer.get().getId()));
        if (validUserTokens.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(401)
                    .message("Token không hợp lệ")
                    .data(null)
                    .build());
        } else if (!jwtService.isTokenValid(request.getToken(), customer.get())) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(401)
                    .message("Token không hợp lệ")
                    .data(null)
                    .build());
        };

        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Token hợp lệ")
                .data(customer.get())
                .build());
    }

    private void saveCustomerToken(Customer customer, String jwtToken) {
        var token = Token.builder()
                .customer(customer)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Customer user) {
        var validUserTokens = tokenRepository.findAllValidTokenByCustomer(Long.valueOf(user.getId()));
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
                saveCustomerToken(user, accessToken);
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

    public ResponseEntity<ResponseObject> updateCustomer (CustomerUpdateRequest customerUpdateRequest){
        Integer customerId = UserUtil.getCurrentCustomerId();
        if (customerId == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(401)
                    .message("Unauthorized")
                    .data(null)
                    .build());
        }
        Optional<Customer> customer = userRepository.findById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Không tìm thấy thông tin khách hàng")
                    .data(null)
                    .build());
        }
        customer.get().setName(customerUpdateRequest.getName());
        customer.get().setAddress(customerUpdateRequest.getAddress());
        customer.get().setPhone(customerUpdateRequest.getPhone());
        customer.get().setDateOfBirth(customerUpdateRequest.getDateOfBirth());
        customer.get().setGender(customerUpdateRequest.getGender());
        userRepository.save(customer.get());
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(customer.get())
                .build());

    }

    public ResponseEntity<ResponseObject> updatePassword (CustomerUpdatePasswordRequest customerUpdateRequest){
        Integer customerId = UserUtil.getCurrentCustomerId();
        if (customerId == null ) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(401)
                    .message("Unauthorized")
                    .data(null)
                    .build());
        }
        Optional<Customer> customer = userRepository.findById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(404)
                    .message("Không tìm thấy thông tin khách hàng")
                    .data(null)
                    .build());
        }
        if(customerUpdateRequest.getPassword() == null || customerUpdateRequest.getNewPassword() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Mật khẩu không được để trống")
                    .data(null)
                    .build());
        }
        if(!passwordEncoder.matches(customerUpdateRequest.getPassword(), customer.get().getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Mật khẩu cũ không đúng")
                    .data(null)
                    .build());
        }
        customer.get().setPassword(passwordEncoder.encode(customerUpdateRequest.getPassword()));
        userRepository.save(customer.get());
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(customer.get())
                .build());

    }
}