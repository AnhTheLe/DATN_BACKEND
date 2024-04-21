package com.projectcnw.salesmanagement.repositories.ProductManagerRepository.NonJPARepository;

import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Variant;

import java.time.LocalDateTime;
import java.util.List;

public interface NonJpaVariantRepository {
    List<Variant> getAllVariantsFilter(int page, int size, String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate);

    int countVariant(String query, List<Integer> categoryIdList, LocalDateTime startDate, LocalDateTime endDate);
}
