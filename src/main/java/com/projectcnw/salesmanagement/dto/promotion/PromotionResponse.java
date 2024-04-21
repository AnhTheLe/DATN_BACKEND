package com.projectcnw.salesmanagement.dto.promotion;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Category;
import com.projectcnw.salesmanagement.models.enums.PromotionEnumType;
import com.projectcnw.salesmanagement.models.enums.PromotionPolicyApplyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PromotionResponse {
    private Integer id;
    private String description;

    @Enumerated(EnumType.STRING)
    private PromotionPolicyApplyType policyApply;

    private String title;
    private Integer value;

    @Enumerated(EnumType.STRING)
    private PromotionEnumType valueType;

    private Date startDate;
    private Date endDate;

    private boolean active;

}
