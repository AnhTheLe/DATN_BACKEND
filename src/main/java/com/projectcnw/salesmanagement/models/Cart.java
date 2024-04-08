package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectcnw.salesmanagement.models.enums.CartStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cart")
public class Cart extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "session_id")
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartStatus status;

    @Column(name = "line1")
    private String line1;

    @Column(name = "line2")
    private String line2;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "country")
    private String country;

    @Column(name = "content")
    private String content;

    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    private List<CartItem> cartItems;

    // Getters and setters
}
