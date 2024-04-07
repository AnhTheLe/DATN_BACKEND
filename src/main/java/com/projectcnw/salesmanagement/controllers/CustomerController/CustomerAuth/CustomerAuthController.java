package com.projectcnw.salesmanagement.controllers.CustomerController.CustomerAuth;


import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.auth.AuthDto;
import com.projectcnw.salesmanagement.dto.auth.AuthResponse;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerAuthDto;
import com.projectcnw.salesmanagement.dto.customer.CustomerAuth.CustomerLoginDto;
import com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth.CustomerAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/auth")
@RequiredArgsConstructor
public class CustomerAuthController {
    private final CustomerAuthService customerAuthService;

    @PostMapping("login")
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

    @PostMapping("register")
    public ResponseEntity<ResponseObject> register(
            @RequestBody @Valid CustomerAuthDto authDto
    ) {
        return customerAuthService.register(authDto);
    }

}
