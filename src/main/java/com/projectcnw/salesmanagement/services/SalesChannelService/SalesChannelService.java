package com.projectcnw.salesmanagement.services.SalesChannelService;

import com.projectcnw.salesmanagement.converter.SalesChannelMapper;
import com.projectcnw.salesmanagement.dto.SalesChannelDTO.SalesChannelDTO;
import com.projectcnw.salesmanagement.models.SalesChannel;
import com.projectcnw.salesmanagement.repositories.SaleChannelRepository.SalesChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalesChannelService {

    private final SalesChannelRepository salesChannelRepository;

    private final SalesChannelMapper salesChannelMapper;

    @Transactional
    public SalesChannelDTO createSalesChannel(SalesChannelDTO salesChannelDTO) {
        SalesChannel salesChannel = salesChannelMapper.toEntity(salesChannelDTO);
        SalesChannel createdSalesChannel = salesChannelRepository.save(salesChannel);
        return salesChannelMapper.toDTO(createdSalesChannel);
    }
}