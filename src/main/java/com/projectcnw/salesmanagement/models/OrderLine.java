package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectcnw.salesmanagement.models.Products.Variant;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine extends BaseEntity {

    private int quantity;

    @Column(name = "return_quantity", columnDefinition = "integer default 0")
    private int returnQuantity;

    private int price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "variant_id")
    private Variant variant;
}

