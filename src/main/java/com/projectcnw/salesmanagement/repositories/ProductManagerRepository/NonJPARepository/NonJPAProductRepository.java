package com.projectcnw.salesmanagement.repositories.ProductManagerRepository.NonJPARepository;

import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NonJPAProductRepository {
    List<BaseProduct> getAllProduct(int page, int size, String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate);

    int countProducts(int page, int size, String query, List<Integer> categoryIdList, LocalDateTime startDate, LocalDateTime endDate);
}
