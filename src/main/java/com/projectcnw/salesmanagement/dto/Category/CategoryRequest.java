package com.projectcnw.salesmanagement.dto.Category;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Data
@Getter
public class CategoryRequest {
    private String title;
    @Nullable
    private String slug;
    private String description;
    @Nullable
    private String metaTitle;
    private List<Integer> productIds;
}
