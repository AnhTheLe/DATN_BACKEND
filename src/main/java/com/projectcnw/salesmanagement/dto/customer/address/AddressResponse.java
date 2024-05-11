package com.projectcnw.salesmanagement.dto.customer.address;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AddressResponse {
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

}
