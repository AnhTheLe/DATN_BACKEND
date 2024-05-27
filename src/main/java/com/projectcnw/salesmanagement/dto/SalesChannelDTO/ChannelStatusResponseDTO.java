package com.projectcnw.salesmanagement.dto.SalesChannelDTO;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelStatusResponseDTO {

    private Integer channelId;
    private String channelName;
    private boolean active;

}
