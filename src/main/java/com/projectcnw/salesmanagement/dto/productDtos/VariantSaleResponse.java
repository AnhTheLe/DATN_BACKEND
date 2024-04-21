package com.projectcnw.salesmanagement.dto.productDtos;

import com.projectcnw.salesmanagement.dto.promotion.PromotionResponse;
import com.projectcnw.salesmanagement.models.Promotion;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VariantSaleResponse {
    private Integer id;
    private String name;

    private int quantity;

    private int importPrice;

    private int retailPrice;

    private int wholeSalePrice;
    private String image;

    private String sku;

    private String barcode;
    private int baseId;

    private String value1;

    private String value2;

    private String value3;

    private int discount;

    private int discountedPrice;

    private PromotionResponse promotion;
}
