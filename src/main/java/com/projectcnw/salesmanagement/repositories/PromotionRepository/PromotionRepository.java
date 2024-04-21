package com.projectcnw.salesmanagement.repositories.PromotionRepository;

import com.projectcnw.salesmanagement.dto.promotion.PromotionResponse;
import com.projectcnw.salesmanagement.models.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query(value = "SELECT p.* FROM promotion p LEFT JOIN promotion_product pp ON p.id = pp.promotion_id \n" +
            "INNER JOIN base_product bp ON pp.product_id = bp.id \n" +
            "INNER JOIN variant v ON bp.id = v.base_id \n" +
            "WHERE v.id = :variantId AND bp.is_deleted = false AND p.active = true " +
            "ORDER BY p.created_at DESC"
            , nativeQuery = true)
    List<Promotion> getAllPromotionByVariantId(@Param("variantId") int variantId);
}
