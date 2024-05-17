package com.projectcnw.salesmanagement.dto.productDtos;

import com.projectcnw.salesmanagement.dto.BaseDto;
import com.projectcnw.salesmanagement.dto.Category.CategoryResponse;
import com.projectcnw.salesmanagement.models.Products.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseProductDto extends BaseDto {
    @NotNull(message = "Tên sản phẩm không được trống")
    @NotBlank(message = "Tên sản phẩm không được trống")
    private String name;

    private List<Integer> categoryIds;

    private String label;
    private int variantNumber;
    private int quantity;
    @NotNull(message = "Thuộc tính không được trống")
    @NotBlank(message = "Thuộc tính không được trống")
    private String attribute1;

    private String attribute2;

    private String description;

    private String attribute3;
    @NotEmpty(message = "Phải có ít nhất 1 phiên bản")
    private List<VariantDto> variants;

    private List<CategoryResponse> listCategories;

}
