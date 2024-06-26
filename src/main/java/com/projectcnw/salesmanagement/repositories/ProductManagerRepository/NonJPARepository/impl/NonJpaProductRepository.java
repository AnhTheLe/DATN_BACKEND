package com.projectcnw.salesmanagement.repositories.ProductManagerRepository.NonJPARepository.impl;

import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.NonJPARepository.NonJPAProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
@Slf4j
public class NonJpaProductRepository implements NonJPAProductRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<BaseProduct> getAllProduct(int page, int size, String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate, List<String> channels) {
        String baseQuery = "SELECT bp.* " +
                "FROM base_product bp " +
                "LEFT JOIN variant v ON bp.id = v.base_id AND v.is_deleted = false ";
        Query productQuery = buildNativeQuery(baseQuery, query, categoryIds, startDate, endDate, channels);
        productQuery.setFirstResult((page) * size);
        productQuery.setMaxResults(size);
        return (List<BaseProduct>) productQuery.getResultList();
    }

    @Override
    public int countProducts(int page, int size, String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate, List<String> channels) {
        String baseQuery = "SELECT bp.* " +
                "FROM base_product bp " +
                "LEFT JOIN variant v ON bp.id = v.base_id AND v.is_deleted = false ";
        Query productQuery = buildNativeQuery(baseQuery, query, categoryIds, startDate, endDate, channels);
        productQuery.setFirstResult(0);
        log.info("countProducts: " + productQuery);
        return productQuery.getResultList().size();
    }

    private Query buildNativeQuery(String baseQuery, String query, List<Integer> categoryIds, LocalDateTime startDate, LocalDateTime endDate, List<String> channels) {
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
            filterQuery.append("WHERE bp.is_deleted = false ");
            whereAdded = true;
        } else {
            filterQuery.append("AND bp.is_deleted = false ");
        }
        if (startDate != null) {
            filterQuery.append("AND bp.created_at >= :startDate ");
        }
        if (endDate != null) {
            filterQuery.append("AND bp.created_at <= :endDate ");
        }
        if (query != null && !query.isEmpty()) {
            filterQuery.append("AND (bp.name LIKE :query OR bp.label LIKE :query) ");
        }
        filterQuery.append("GROUP BY bp.id, bp.name, bp.is_deleted " +
                "ORDER BY bp.created_at DESC ");
        Query productQuery = entityManager.createNativeQuery(filterQuery.toString(), BaseProduct.class);
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
