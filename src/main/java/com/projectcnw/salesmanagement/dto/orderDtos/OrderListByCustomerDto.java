package com.projectcnw.salesmanagement.dto.orderDtos;

import com.projectcnw.salesmanagement.models.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderListByCustomerDto {
    private Integer orderId;
    private String staffFullName;
    private int discount;
    private List<OrderLineDTO> orderLines;
    private Payment payment;
    private String address;
    private String phone;
    private String customerName;
}
