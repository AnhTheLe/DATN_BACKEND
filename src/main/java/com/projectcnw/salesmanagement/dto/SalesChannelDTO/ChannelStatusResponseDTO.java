package com.projectcnw.salesmanagement.dto.SalesChannelDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelStatusResponseDTO {

    private Integer channelId;
    private String channelName;
    private boolean active;

}
