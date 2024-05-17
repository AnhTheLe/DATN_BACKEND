package com.projectcnw.salesmanagement.dto.promotion;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Category;
import com.projectcnw.salesmanagement.models.enums.PromotionEnumType;
import com.projectcnw.salesmanagement.models.enums.PromotionPolicyApplyType;
import com.projectcnw.salesmanagement.models.enums.PromotionStatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PromotionRequest {
    private String title;
    private Integer value;
    private Date startDate;
    private Date endDate;

    private PromotionEnumType valueType;

    private PromotionPolicyApplyType policyApply;

    private PromotionStatusType status;

    private String description;

    private List<Integer> productIds;

    private List<Integer> categoryIds;
}

