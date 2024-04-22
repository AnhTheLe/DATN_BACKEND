package com.projectcnw.salesmanagement.dto.shopping_cart;

import lombok.Data;

@Data
public class ShoppingCartRequest {
    private Integer productId;
    private Integer quantity;
}
