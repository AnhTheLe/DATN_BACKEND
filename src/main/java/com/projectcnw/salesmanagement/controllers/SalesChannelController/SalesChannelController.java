package com.projectcnw.salesmanagement.controllers.SalesChannelController;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.SalesChannelDTO.SalesChannelDTO;
import com.projectcnw.salesmanagement.services.SalesChannelService.SalesChannelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/sales-channels")
@RequiredArgsConstructor
public class SalesChannelController {

    private final SalesChannelService salesChannelService;

    @PostMapping
    public ResponseEntity<ResponseObject> createSalesChannel(@RequestBody @Valid SalesChannelDTO salesChannelDTO) {
        SalesChannelDTO createdSalesChannel = salesChannelService.createSalesChannel(salesChannelDTO);
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(createdSalesChannel)
                .build()
        );
    }
}
