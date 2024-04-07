package com.projectcnw.salesmanagement.dto.customer.CustomerAuth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomerLoginDto {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
