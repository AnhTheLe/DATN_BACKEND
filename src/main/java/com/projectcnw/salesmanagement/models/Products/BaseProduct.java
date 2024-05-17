package com.projectcnw.salesmanagement.models.Products;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projectcnw.salesmanagement.models.BaseEntity;
import com.projectcnw.salesmanagement.models.Promotion;
import com.projectcnw.salesmanagement.models.Shop;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "base_product")
public class BaseProduct extends BaseEntity {

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String label;

    private String attribute1;

    private String attribute2;

    private String attribute3;

    private String description;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "baseProduct", fetch = FetchType.EAGER)
    private List<Variant> variants;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToMany(mappedBy = "products", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Category> categories;


    @ManyToMany(mappedBy = "products")
    @JsonBackReference
    private List<Promotion> promotions;

}
