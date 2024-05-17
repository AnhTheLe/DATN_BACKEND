package com.projectcnw.salesmanagement.repositories.PromotionRepository.NonJpaPromotionRepository;

import com.projectcnw.salesmanagement.models.Promotion;
import com.projectcnw.salesmanagement.models.enums.PromotionPolicyApplyType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NonJpaPromotionRepositoryImpl implements NonJpaPromotionRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Promotion> getAllPromotionFilter(int page, int size, String query, String policyApply, String status, LocalDateTime startDate, String sortBy, String order) {
        String baseQuery = "SELECT p.* " +
                "FROM promotion p WHERE 1=1 \n";
        Query promotionQuery = buildNativeQuery(baseQuery, query, policyApply, status, startDate, sortBy, order);
        promotionQuery.setFirstResult((page) * size);
        promotionQuery.setMaxResults(size);
        return (List<Promotion>) promotionQuery.getResultList();
    }

    @Override
    public int countPromotion(String query, String policyApply, String status, LocalDateTime startDate, String sortBy, String order) {
        String baseQuery = "SELECT p.* " +
                "FROM promotion p WHERE 1=1 \n";
        Query productQuery = buildNativeQuery(baseQuery, query, policyApply, status, startDate, "id", "desc");
        productQuery.setFirstResult(0);
        return productQuery.getResultList().size();
    }

    private Query buildNativeQuery(String baseQuery, String query, String policyApply, String status, LocalDateTime startDate, String sortBy, String order) {
        StringBuilder filterQuery = new StringBuilder(baseQuery);

        if (startDate != null) {
            filterQuery.append("AND p.start_date >= :startDate \n");
        }

        if(status != null && !status.isEmpty()){
            filterQuery.append("AND p.status = :status \n");
        }

        if (query != null && !query.isEmpty()) {
            filterQuery.append("AND p.title LIKE :query \n");
        }
        if (policyApply != null && !policyApply.isEmpty()) {
            if (policyApply.equals(PromotionPolicyApplyType.COUPON.name())) {
                filterQuery.append("AND p.policy_apply = 'COUPON' \n");
            } else {
                filterQuery.append("AND p.policy_apply <> 'COUPON' \n");
            }
        }
        filterQuery.append("GROUP BY p.id, p.title \n");
        if (!sortBy.isEmpty()) {
            filterQuery.append("ORDER BY p.").append(sortBy).append(" ").append(order.toUpperCase());
        } else {
            filterQuery.append("ORDER BY p.").append("id").append(" ").append(order.toUpperCase());
        }
        Query promotionQuery = entityManager.createNativeQuery(filterQuery.toString(), Promotion.class);

        if (startDate != null) {
            promotionQuery.setParameter("startDate", startDate);
        }

        if (query != null && !query.isEmpty()) {
            promotionQuery.setParameter("query", "%" + query + "%");
        }
        if(status != null && !status.isEmpty()) promotionQuery.setParameter("status", status);

//        if(!sortBy.isEmpty()){
//            promotionQuery.setParameter("sortBy", sortBy);
//        }
//
//        if(!order.isEmpty()){
//            promotionQuery.setParameter("order", order);
//        }

        return promotionQuery;
    }
}
