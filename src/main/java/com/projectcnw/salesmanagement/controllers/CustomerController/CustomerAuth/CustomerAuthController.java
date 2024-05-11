package com.projectcnw.salesmanagement.controllers.CustomerController.CustomerAuth;


import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.auth.AuthResponse;
import com.projectcnw.salesmanagement.dto.auth.VerifyTokenRequest;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerAuthDto;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerLoginDto;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerUpdatePasswordRequest;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerUpdateRequest;
import com.projectcnw.salesmanagement.models.Customer;
import com.projectcnw.salesmanagement.repositories.CustomerRepositories.CustomerRepository;
import com.projectcnw.salesmanagement.repositories.TokenRepository;
import com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth.CustomerAuthService;
import com.projectcnw.salesmanagement.services.CustomerServices.CustomerServices;
import com.projectcnw.salesmanagement.services.auth.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerAuthController {
    private final CustomerAuthService customerAuthService;
    private final CustomerServices customerServices;


    @PostMapping("/auth/login")
    public ResponseEntity<ResponseObject> login(
            @RequestBody @Valid CustomerLoginDto authDto
    ) {

        AuthResponse authResponse = customerAuthService.authenticate(authDto);

        return ResponseEntity.ok(ResponseObject.builder()
                .data(authResponse)
                .message("login successfully")
                .responseCode(HttpStatus.OK.value())
                .build());
    }

    @PostMapping("/auth/verify-token")
    public ResponseEntity<ResponseObject> verifyToken(
            @RequestBody @Valid VerifyTokenRequest request
    ) {
        return customerAuthService.verifyToken(request);
    }


    @PostMapping("/auth/register")
    public ResponseEntity<ResponseObject> register(
            @RequestBody @Valid CustomerAuthDto authDto
    ) {
        return customerAuthService.register(authDto);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseObject> getCustomerInfo(@RequestHeader HttpHeaders headers) {
        String authHeader = headers.getFirst("Authorization");
        if (authHeader == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseObject.builder()
                    .responseCode(401)
                    .message("Authorization header is required")
                    .build());
        }
        String accessToken = authHeader.substring(7);
        return customerAuthService.getCustomerInfo(accessToken);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseObject> updateCustomerInfo( @RequestBody CustomerUpdateRequest customerAuthDto) {

        return customerAuthService.updateCustomer(customerAuthDto);
    }

    @PutMapping("/update-password")
    public ResponseEntity<ResponseObject> updatePassword(@RequestBody CustomerUpdatePasswordRequest customerAuthDto) {
        return customerAuthService.updatePassword(customerAuthDto);
    }

}
