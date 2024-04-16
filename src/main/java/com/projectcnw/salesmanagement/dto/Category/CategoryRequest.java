package com.projectcnw.salesmanagement.dto.Category;

import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Data
@Getter
public class CategoryRequest {
    private String title;
    private String slug;
    private String description;
    private String metaTitle;
    private List<Integer> productIds;
    private Date startDate;
    private Date endDate;
}
