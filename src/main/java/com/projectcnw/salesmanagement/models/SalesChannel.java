package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "sales_channel")
public class SalesChannel extends BaseEntity {

    @NotBlank(message = "Channel name is mandatory")
    private String name;

    private String description;

    private String code;

    @ManyToMany
    @JoinTable(
            name = "base_product_sales_channel",
            joinColumns = @JoinColumn(name = "sales_channel_id"),
            inverseJoinColumns = @JoinColumn(name = "base_product_id")
    )
    @JsonManagedReference
    private List<BaseProduct> products;

    @OneToMany(mappedBy = "salesChannel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

}
