package com.projectcnw.salesmanagement.dto.productDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopSaleVariant {
    private VariantSaleResponse variant;
    private BigDecimal value;
}
