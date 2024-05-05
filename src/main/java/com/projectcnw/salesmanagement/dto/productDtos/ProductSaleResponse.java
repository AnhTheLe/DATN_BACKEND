package com.projectcnw.salesmanagement.dto.productDtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaleResponse {
    private String name;

    private List<Integer> categoryIds;

    private String label;
    private int variantNumber;
    private int quantity;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private List<String> images;

    private List<VariantSaleResponse> variants;
}
