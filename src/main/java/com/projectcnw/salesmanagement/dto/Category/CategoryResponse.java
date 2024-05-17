package com.projectcnw.salesmanagement.dto.Category;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Integer id;
    private String title;
    private String description;
    private String slug;
    private Integer productCount;
    private String metaTitle;

}
