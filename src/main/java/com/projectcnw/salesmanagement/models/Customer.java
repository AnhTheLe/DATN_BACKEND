package com.projectcnw.salesmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projectcnw.salesmanagement.models.Auth.Role;
import com.projectcnw.salesmanagement.models.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity implements UserDetails {
    @NotNull(message = "name is not null")
    private String name;

    private String customerCode;

    private String address;

    private String email;

    private Date dateOfBirth;

    private String password;

    @NotNull(message = "phone is not null")
    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "group_id")
//    @JsonIgnore
    private CustomerGroup group;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Feedback> feedbackList;

    @ManyToOne()
    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "role_id", columnDefinition = "integer default 5")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName().name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
