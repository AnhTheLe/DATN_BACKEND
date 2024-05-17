package com.projectcnw.salesmanagement.dto.orderDtos;

import com.projectcnw.salesmanagement.models.Products.Variant;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLineDTO {
    private int quantity;
    private int returnQuantity;
    private int price;
    private Variant variant;

}
