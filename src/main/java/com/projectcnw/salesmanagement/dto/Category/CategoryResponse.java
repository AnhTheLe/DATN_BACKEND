package com.projectcnw.salesmanagement.dto.Category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryResponse {
    private Integer id;
    private String title;
    private String description;
    private String slug;
    private Integer productCount;
    private String metaTitle;

}
