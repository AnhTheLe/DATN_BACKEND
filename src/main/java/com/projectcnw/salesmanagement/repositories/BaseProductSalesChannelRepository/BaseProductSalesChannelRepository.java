package com.projectcnw.salesmanagement.repositories.BaseProductSalesChannelRepository;

import com.projectcnw.salesmanagement.models.BaseProductSalesChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseProductSalesChannelRepository extends JpaRepository<BaseProductSalesChannel, Long> {

    List<BaseProductSalesChannel> findByBaseProduct_Id(Integer id);
}