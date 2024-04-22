package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectcnw.salesmanagement.models.Products.Variant;
import com.projectcnw.salesmanagement.models.enums.CartStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@Table(name = "shopping_cart", uniqueConstraints =@UniqueConstraint(columnNames = {"customer_id", "variant_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id")
    @JsonIgnore
    private Variant variant;

    @Column(name = "quantity")
    private int quantity;

}
