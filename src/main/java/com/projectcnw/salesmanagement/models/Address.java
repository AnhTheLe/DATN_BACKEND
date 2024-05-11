package com.projectcnw.salesmanagement.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Address extends BaseEntity {

    private String address;

    private Boolean isDefault;

    private String city;

    private String company;

    private String country;

    private String countryCode;

    private String province;

    private String provinceCode;

    private String district;

    private String districtCode;

    private String wardCode;

    private String ward;

    private String phone;

    private String zip;

    private String customerName;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
