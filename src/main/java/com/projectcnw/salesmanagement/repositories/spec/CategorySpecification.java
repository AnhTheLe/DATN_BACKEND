package com.projectcnw.salesmanagement.repositories.spec;

import com.projectcnw.salesmanagement.models.Products.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> hasNameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.<String>get("title"), "%" + name + "%");
    }


}
