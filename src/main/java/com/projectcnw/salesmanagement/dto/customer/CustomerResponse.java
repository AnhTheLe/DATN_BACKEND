package com.projectcnw.salesmanagement.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectcnw.salesmanagement.models.Address;
import com.projectcnw.salesmanagement.models.CustomerGroup;
import com.projectcnw.salesmanagement.models.Feedback;
import com.projectcnw.salesmanagement.models.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

public class CustomerResponse {

    private String name;

    private String customerCode;

    private String address;

    private String email;

    private Date dateOfBirth;

    private String password;

    private String phone;

    private Gender gender;

    private CustomerGroup group;

    private List<Feedback> feedbackList;

    private List<Address> Addresses;

    private Address addressDefault;
}
