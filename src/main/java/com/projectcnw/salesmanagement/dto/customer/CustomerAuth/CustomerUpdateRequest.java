package com.projectcnw.salesmanagement.dto.customer.CustomerAuth;

import com.projectcnw.salesmanagement.models.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomerUpdateRequest {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String newPassword;
    private String address;
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
