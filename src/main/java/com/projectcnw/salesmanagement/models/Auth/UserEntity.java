package com.projectcnw.salesmanagement.models.Auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import com.projectcnw.salesmanagement.models.Auth.Role;
import com.projectcnw.salesmanagement.models.BaseEntity;
import com.projectcnw.salesmanagement.models.Shop;
import com.projectcnw.salesmanagement.models.WarehouseBalance;
import com.projectcnw.salesmanagement.models.enums.Gender;
import com.projectcnw.salesmanagement.models.enums.WorkStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class UserEntity extends BaseEntity implements UserDetails {

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "is_active", columnDefinition = "boolean default false")
    private boolean isActive;

    private String password;

    @Column(unique = true)
    private String phone;

    private String address;

    private Date dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private WorkStatus workStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    private List<Role> roles;

    @OneToMany(mappedBy = "userEntity")
    private List<WarehouseBalance> warehouseBalanceList;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonIgnore
    private Shop shop;

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
