package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private BaseProduct products;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private ShoppingCart cart;

    @Column(name = "sku")
    private String sku;

    @Column(name = "price")
    private double price;

    @Column(name = "discount")
    private double discount;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "active")
    private boolean active;

    @Column(name = "content")
    private String content;

    // Getters and setters
}
