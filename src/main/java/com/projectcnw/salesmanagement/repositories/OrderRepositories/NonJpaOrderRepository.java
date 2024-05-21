package com.projectcnw.salesmanagement.repositories.OrderRepositories;

import com.projectcnw.salesmanagement.dto.orderDtos.IOrderListItemDto;
import com.projectcnw.salesmanagement.models.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NonJpaOrderRepository {
    List<Order> getOrderFilter(int page, int size, String query, LocalDateTime startDate, LocalDateTime endDate, List<String> listSaleChannel);
    public int countOrder(String query, LocalDateTime startDate, LocalDateTime endDate, List<String> listSaleChannel) ;
}
