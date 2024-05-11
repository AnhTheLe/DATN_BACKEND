package com.projectcnw.salesmanagement.dto.customer.CustomerAuth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdatePasswordRequest {
    private String newPassword;
    private String password;
}
