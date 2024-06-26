package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.projectcnw.salesmanagement.models.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_order")
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "person_in_charge")
    private UserEntity userEntity;

    @Column(columnDefinition = "integer default 0")
    private int discount;

    @Column(columnDefinition = "nvarchar(255)")
    private String address;

    @Column(columnDefinition = "nvarchar(255)")
    private String customerName;

    @Column(columnDefinition = "nvarchar(255)")
    private String phone;



    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderLine> orderLineList;

    @OneToOne(mappedBy = "swapOrder")
    private ReturnOrder returnOrder;

    @ManyToOne
    @JoinColumn(name = "sales_channel_id")
    private SalesChannel salesChannel;
}
