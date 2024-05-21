package com.projectcnw.salesmanagement.converter;

import com.projectcnw.salesmanagement.dto.SalesChannelDTO.SalesChannelDTO;
import com.projectcnw.salesmanagement.models.SalesChannel;
import org.springframework.stereotype.Component;

@Component
public class SalesChannelMapper {

    public SalesChannel toEntity(SalesChannelDTO dto) {
        SalesChannel entity = new SalesChannel();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public SalesChannelDTO toDTO(SalesChannel entity) {
        SalesChannelDTO dto = new SalesChannelDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
