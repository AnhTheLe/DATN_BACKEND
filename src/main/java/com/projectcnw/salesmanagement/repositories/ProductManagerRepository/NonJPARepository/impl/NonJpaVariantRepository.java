package com.projectcnw.salesmanagement.repositories.ProductManagerRepository.NonJPARepository.impl;

import com.projectcnw.salesmanagement.models.Products.Variant;
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
public class NonJpaVariantRepository implements com.projectcnw.salesmanagement.repositories.ProductManagerRepository.NonJPARepository.NonJpaVariantRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Variant> getAllVariantsFilter(int page, int size, String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate, String sortBy, String order, List<String> channels) {
        String baseQuery = "SELECT v.* " +
                "FROM variant v " +
                "LEFT JOIN base_product bp ON bp.id = v.base_id AND bp.is_deleted = false ";
        Query productQuery = buildNativeQuery(baseQuery, query, categoryIds, startDate, endDate, sortBy, order, channels);
        productQuery.setFirstResult((page - 1) * size);
        productQuery.setMaxResults(size);
        return (List<Variant>) productQuery.getResultList();
    }

    @Override
    public int countVariant(String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate, List<String> channels) {
        String baseQuery = "SELECT v.* " +
                "FROM variant v " +
                "LEFT JOIN base_product bp ON bp.id = v.base_id AND bp.is_deleted = false ";
        Query productQuery = buildNativeQuery(baseQuery, query, categoryIds, startDate, endDate, "created_at", "desc", channels);
        productQuery.setFirstResult(0);
        return productQuery.getResultList().size();
    }

    private Query buildNativeQuery(String baseQuery, String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate, String sortBy, String order, List<String> channels) {
        StringBuilder filterQuery = new StringBuilder(baseQuery);
        boolean whereAdded = false;

        if (channels != null && !channels.isEmpty()) {
            filterQuery.append(" INNER JOIN base_product_sales_channel bpsc ON bp.id = bpsc.base_product_id " +
                    "INNER JOIN sales_channel sc ON bpsc.sales_channel_id = sc.id ");
            if (!whereAdded) {
                filterQuery.append("WHERE ");
                whereAdded = true;
            }
            filterQuery.append("sc.code IN (:channels) AND bpsc.active = true ");
        }

        if (categoryIds != null && !categoryIds.isEmpty()) {
            filterQuery.append(" INNER JOIN product_category bpc ON bp.id = bpc.product_id " +
                    "INNER JOIN category c ON bpc.category_id = c.id ");
            if (!whereAdded) {
                filterQuery.append("WHERE ");
                whereAdded = true;
            } else {
                filterQuery.append("AND ");
            }
            filterQuery.append("c.id IN (:categoryIds) ");
        }

        if (!whereAdded) {
            filterQuery.append("WHERE v.is_deleted = false ");
            whereAdded = true;
        } else {
            filterQuery.append("AND v.is_deleted = false ");
        }

        if (startDate != null) {
            filterQuery.append("AND v.created_at >= :startDate ");
        }
        if (endDate != null) {
            filterQuery.append("AND v.created_at <= :endDate ");
        }
        if (query != null && !query.isEmpty()) {
            filterQuery.append("AND v.name LIKE :query ");
        }

        filterQuery.append("GROUP BY v.id, v.name, v.is_deleted ");
        if ("price".equals(sortBy)) {
            filterQuery.append("ORDER BY v.retail_price ").append(order.toUpperCase());
        } else {
            filterQuery.append("ORDER BY v.").append(sortBy).append(" ").append(order.toUpperCase());
        }

        Query productQuery = entityManager.createNativeQuery(filterQuery.toString(), Variant.class);

        if (channels != null && !channels.isEmpty()) {
            productQuery.setParameter("channels", channels);
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            productQuery.setParameter("categoryIds", categoryIds);
        }
        if (startDate != null) {
            productQuery.setParameter("startDate", startDate);
        }
        if (endDate != null) {
            productQuery.setParameter("endDate", endDate);
        }
        if (query != null && !query.isEmpty()) {
            productQuery.setParameter("query", "%" + query + "%");
        }

        return productQuery;
    }

}
