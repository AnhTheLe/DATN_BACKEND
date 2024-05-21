package com.projectcnw.salesmanagement.models;

import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "base_product_sales_channel")
public class BaseProductSalesChannel extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "base_product_id")
    private BaseProduct baseProduct;

    @ManyToOne
    @JoinColumn(name = "sales_channel_id")
    private SalesChannel salesChannel;
    
    private boolean active;

}