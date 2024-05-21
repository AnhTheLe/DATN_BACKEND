package com.projectcnw.salesmanagement.repositories.SaleChannelRepository;

import com.projectcnw.salesmanagement.models.SalesChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesChannelRepository extends JpaRepository<SalesChannel, Long> {

    Optional<SalesChannel> findByName(String name);

    Optional<SalesChannel> findSalesChannelByCode(String pos);
}