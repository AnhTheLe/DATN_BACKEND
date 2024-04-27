package com.projectcnw.salesmanagement.models.Auth;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.projectcnw.salesmanagement.models.BaseEntity;
import com.projectcnw.salesmanagement.models.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleType name;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private List<UserEntity> userEntities;

}
