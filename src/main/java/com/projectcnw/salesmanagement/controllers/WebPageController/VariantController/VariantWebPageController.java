package com.projectcnw.salesmanagement.controllers.WebPageController.VariantController;


import com.projectcnw.salesmanagement.dto.PagedResponseObject;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.productDtos.VariantSaleResponse;
import com.projectcnw.salesmanagement.services.ProductManagerServices.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/variants")
@RequiredArgsConstructor
public class VariantWebPageController {

    private final VariantService variantService;

    @GetMapping("")
    public ResponseEntity<PagedResponseObject> getAllBaseProduct(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                 @RequestParam(name = "query", defaultValue = "") String query,
                                                                 @RequestParam(name = "categoryIds", defaultValue = "") String categoryIds,
                                                                 @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                                                 @RequestParam(name = "endDate", defaultValue = "") String endDate
    ) {
        List<VariantSaleResponse> products = variantService.getAllVariantsFilter(page, size, query, categoryIds, startDate, endDate);
        long totalItems = variantService.countVariantWebPage(query, categoryIds, startDate, endDate);
        int totalPages = (int) Math.ceil((double) totalItems / size);
        return ResponseEntity.ok(PagedResponseObject.builder()
                .page(page)
                .perPage(size)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .responseCode(200)
                .message("Success")
                .data(products)
                .build());
    }

    @GetMapping("/top-discount")
    public ResponseEntity<ResponseObject> getTop10VariantSale() {
        List<VariantSaleResponse> products = variantService.getTop10VariantHasPromotion();
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(products)
                .build());
    }

}
