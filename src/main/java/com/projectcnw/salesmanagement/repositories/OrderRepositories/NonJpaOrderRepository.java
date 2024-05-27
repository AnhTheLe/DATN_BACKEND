package com.projectcnw.salesmanagement.repositories.OrderRepositories;

import com.projectcnw.salesmanagement.models.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface NonJpaOrderRepository {
    List<Order> getOrderFilter(int page, int size, String query, LocalDateTime startDate, LocalDateTime endDate, List<String> listSaleChannel, List<String> listStatus);

    public int countOrder(String query, LocalDateTime startDate, LocalDateTime endDate, List<String> listSaleChannel, List<String> listStatus);
}
