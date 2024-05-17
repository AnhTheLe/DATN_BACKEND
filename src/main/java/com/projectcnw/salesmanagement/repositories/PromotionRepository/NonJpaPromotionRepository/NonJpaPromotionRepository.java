package com.projectcnw.salesmanagement.repositories.PromotionRepository.NonJpaPromotionRepository;

import com.projectcnw.salesmanagement.models.Products.Variant;
import com.projectcnw.salesmanagement.models.Promotion;

import java.time.LocalDateTime;
import java.util.List;

public interface NonJpaPromotionRepository {
    List<Promotion> getAllPromotionFilter(int page, int size, String query,String policyApply,String status, LocalDateTime startDate, String sortBy, String order);

    public int countPromotion(String query, String policyApply, String status, LocalDateTime startDate, String sortBy, String order);
}
