package com.projectcnw.salesmanagement.repositories.PromotionRepository;

import com.projectcnw.salesmanagement.models.Promotion;
import com.projectcnw.salesmanagement.models.enums.PromotionPolicyApplyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query(value = "(SELECT p.* FROM promotion p " +
            "LEFT JOIN promotion_product pp ON p.id = pp.promotion_id " +
            "INNER JOIN base_product bp ON pp.product_id = bp.id " +
            "INNER JOIN variant v ON bp.id = v.base_id " +
            "WHERE v.id = :variantId AND bp.is_deleted = false AND p.status = 'active' " +
            "ORDER BY p.created_at DESC) " +
            "UNION " +
            "(SELECT p.* FROM promotion p " +
            "WHERE p.policy_apply = 'ALL' AND p.status = 'active' " +
            "ORDER BY p.created_at DESC)", nativeQuery = true)
    List<Promotion> getAllPromotionByVariantId(@Param("variantId") int variantId);


    // get Promotion with coupon title
    @Query(value = "SELECT p.* FROM promotion p WHERE p.title LIKE :title AND p.status = 'active' AND p.policy_apply = :policyApply \n" +
            "ORDER BY p.created_at DESC"
            , nativeQuery = true)
    List<Promotion> getPromotionsByTitleAndPolicyApplyAndActive(@Param("title") String title,@Param("policyApply") String policyApply);

}
