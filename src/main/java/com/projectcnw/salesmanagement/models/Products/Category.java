package com.projectcnw.salesmanagement.models.Products;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectcnw.salesmanagement.models.BaseEntity;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Promotion;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    private String title;
    private String metaTitle;
    private String slug;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    private List<BaseProduct> products;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "promotion_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private List<Promotion> promotions;



}
