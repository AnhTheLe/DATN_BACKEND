package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {


    @OneToMany(mappedBy = "cartItem")
    @JsonIgnore
    private List<BaseProduct> products;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

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
