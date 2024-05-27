package com.projectcnw.salesmanagement.repositories.OrderRepositories.NonJPARepository.impl;

import com.projectcnw.salesmanagement.models.Order;
import com.projectcnw.salesmanagement.repositories.OrderRepositories.NonJpaOrderRepository;
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
public class NonJpaOrderRepositoryImpl implements NonJpaOrderRepository {

    @PersistenceContext
    private final EntityManager entityManager;


    @Override
    public List<Order> getOrderFilter(int page, int size, String query, LocalDateTime startDate, LocalDateTime endDate, List<String> listSaleChannel, List<String> listStatus) {
        String baseQuery = "select o.*" +
                " from _order o" +
                " JOIN customer c ON o.customer_id = c.id " +
                " LEFT JOIN sales_channel sc ON o.sales_channel_id = sc.id" +
                " INNER JOIN payment p ON p.order_id = o.id AND p.order_type = 'ORDER' " +
                " where 1 = 1 ";
        Query productQuery = buildNativeQuery(baseQuery, query, startDate, endDate, listSaleChannel, listStatus);
        productQuery.setFirstResult((page) * size);
        productQuery.setMaxResults(size);
        return (List<Order>) productQuery.getResultList();
    }

    @Override
    public int countOrder(String query, LocalDateTime startDate, LocalDateTime endDate, List<String> listSaleChannel, List<String> listStatus) {
        String baseQuery = "select o.*" +
                " from _order o" +
                " JOIN customer c ON o.customer_id = c.id " +
                " LEFT JOIN sales_channel sc ON o.sales_channel_id = sc.id" +
                " INNER JOIN payment p ON p.order_id = o.id AND p.order_type = 'ORDER' " +
                " where 1 = 1 ";
//                " where o.customer_id = c.id" +
//                " and (o.sales_channel_id = sc.id or o.sales_channel_id is null) ";
        Query productQuery = buildNativeQuery(baseQuery, query, startDate, endDate, listSaleChannel, listStatus);
        productQuery.setFirstResult(0);
        return productQuery.getResultList().size();
    }

    private Query buildNativeQuery(String baseQuery, String query, LocalDateTime startDate, LocalDateTime endDate, List<String> channels, List<String> listStatus) {
        StringBuilder filterQuery = new StringBuilder(baseQuery);

        if (listStatus != null && !listStatus.isEmpty()) {
            filterQuery.append(" and p.payment_status in :listStatus ");
        }

        if (channels != null && !channels.isEmpty()) {
            filterQuery.append(" and sc.code in :channels ");
        }

        if (startDate != null) {
            filterQuery.append("AND o.created_at >= :startDate ");
        }
        if (endDate != null) {
            filterQuery.append("AND o.created_at <= :endDate ");
        }
        if (query != null && !query.isEmpty()) {
            filterQuery.append("and " +
                    "              (lower(c.name) like concat('%', lower(:query), '%') \n" +
                    "             or lower(c.phone) like concat('%', lower(:query), '%')) \n");
        }
        filterQuery.append("order by o.created_at desc ");
        Query productQuery = entityManager.createNativeQuery(filterQuery.toString(), Order.class);
        if (channels != null && !channels.isEmpty()) {
            productQuery.setParameter("channels", channels);
        }
        if (listStatus != null && !listStatus.isEmpty()) {
            productQuery.setParameter("listStatus", listStatus);
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
