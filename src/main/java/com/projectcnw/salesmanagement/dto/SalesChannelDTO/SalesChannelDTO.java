package com.projectcnw.salesmanagement.dto.SalesChannelDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesChannelDTO {

    @NotBlank(message = "Channel name is mandatory")
    private String name;

    private String description;

    // getters and setters
}
