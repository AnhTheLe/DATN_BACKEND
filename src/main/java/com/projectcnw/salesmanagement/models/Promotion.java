package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Category;
import com.projectcnw.salesmanagement.models.enums.PromotionEnumType;
import com.projectcnw.salesmanagement.models.enums.PromotionPolicyApplyType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Promotion extends BaseEntity{
    private String title;
    private Integer value;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type")
    private PromotionEnumType valueType;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type")
    private PromotionPolicyApplyType policyApply;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "promotion_product",
            joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<BaseProduct> products;

    @ManyToMany(mappedBy = "promotions")
    @JsonBackReference
    private List<Category> categories;
}
