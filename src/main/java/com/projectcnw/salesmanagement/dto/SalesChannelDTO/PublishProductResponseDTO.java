package com.projectcnw.salesmanagement.dto.SalesChannelDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PublishProductResponseDTO {
    private Long productId;
    private List<ChannelStatusResponseDTO> channelStatus;

}
