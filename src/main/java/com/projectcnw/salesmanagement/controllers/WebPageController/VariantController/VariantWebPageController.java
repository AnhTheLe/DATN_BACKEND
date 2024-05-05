package com.projectcnw.salesmanagement.controllers.WebPageController.VariantController;


import com.projectcnw.salesmanagement.dto.PagedResponseObject;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.productDtos.TopSaleVariant;
import com.projectcnw.salesmanagement.dto.productDtos.VariantSaleResponse;
import com.projectcnw.salesmanagement.services.OrderServices.OrderService;
import com.projectcnw.salesmanagement.services.ProductManagerServices.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/variants")
@RequiredArgsConstructor
public class VariantWebPageController {

    private final VariantService variantService;
    private final OrderService orderService;

    @GetMapping("")
    public ResponseEntity<PagedResponseObject> getAllBaseProduct(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                 @RequestParam(name = "query", defaultValue = "") String query,
                                                                 @RequestParam(name = "categoryIds", defaultValue = "") String categoryIds,
                                                                 @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                                                 @RequestParam(name = "endDate", defaultValue = "") String endDate,
                                                                 @RequestParam(name = "sort_by", defaultValue = "created_at") String sortBy,
                                                                 @RequestParam(name = "order", defaultValue = "desc") String order

    ) {
        List<VariantSaleResponse> products = variantService.getAllVariantsFilter(page, size, query, categoryIds, startDate, endDate, sortBy, order);
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

    @GetMapping("/top-sale")
    public ResponseEntity<ResponseObject> getTop10VariantOrder() {
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() - 30L * 24 * 60 * 60 * 10000);
        Date endDate = new Date();
        List<TopSaleVariant> products = variantService.getTopSaleVariant(startDate, endDate, "order");
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(products.subList(0, Math.min(products.size(), 10)))
                .build());
    }

}
