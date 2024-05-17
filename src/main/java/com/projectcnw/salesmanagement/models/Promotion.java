package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Category;
import com.projectcnw.salesmanagement.models.enums.PromotionEnumType;
import com.projectcnw.salesmanagement.models.enums.PromotionPolicyApplyType;
import com.projectcnw.salesmanagement.models.enums.PromotionStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Promotion extends BaseEntity{
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_apply")
    @JsonProperty("policyApply")
    private PromotionPolicyApplyType policyApply;

    private String title;
    private Integer value;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    @JsonProperty("valueType")
    private PromotionEnumType valueType;

    private Date startDate;
    private Date endDate;


//    @Column(name = "active", columnDefinition = "boolean default true")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20) default 'active'")
    @JsonProperty("status")
    private PromotionStatusType status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "promotion_product",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<BaseProduct> products;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "promotion_category",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;
}
