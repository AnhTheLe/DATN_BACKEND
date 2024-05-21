package com.projectcnw.salesmanagement.dto.SalesChannelDTO;


import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class PublishProductDTO {
    private Integer productId;
    private List<ChannelStatusDTO> channelStatus;

}
