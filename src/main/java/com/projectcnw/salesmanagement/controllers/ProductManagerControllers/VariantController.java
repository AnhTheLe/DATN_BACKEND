package com.projectcnw.salesmanagement.controllers.ProductManagerControllers;

import com.projectcnw.salesmanagement.controllers.BaseController;
import com.projectcnw.salesmanagement.dto.PagedResponseObject;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.productDtos.VariantDto;
import com.projectcnw.salesmanagement.dto.productDtos.VariantSaleResponse;
import com.projectcnw.salesmanagement.services.ProductManagerServices.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class VariantController extends BaseController {

    private final VariantService variantService;

    @GetMapping("/base-products/variants")
    public ResponseEntity<PagedResponseObject> getAllVariants(@RequestParam(name = "page", defaultValue = "1") int page,
                                                              @RequestParam(name = "size", defaultValue = "10") int size) {
        long totalItems = variantService.countVariant();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        List<VariantSaleResponse> variantDtos = variantService.getAllVariants(page, size);
        return ResponseEntity.ok(PagedResponseObject.builder()
                .page(page)
                .perPage(size)
                .totalPages(totalPages)
                .totalItems(totalItems)
                .responseCode(200)
                .message("Success")
                .data(variantDtos)
                .build());
    }
    @GetMapping("/base-products/variants/filter")
    public ResponseEntity<PagedResponseObject> getAllBaseProduct(@RequestParam(name = "page", defaultValue = "1") int page,
                                                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                                                 @RequestParam(name = "query", defaultValue = "") String query,
                                                                 @RequestParam(name = "categoryIds", defaultValue = "") String categoryIds,
                                                                 @RequestParam(name = "startDate", defaultValue = "") String startDate,
                                                                 @RequestParam(name = "endDate", defaultValue = "") String endDate,
                                                                 @RequestParam(name = "sort_by", defaultValue = "created_at") String sortBy,
                                                                 @RequestParam(name = "order", defaultValue = "desc") String order,
                                                                 @RequestParam(name = "channels", defaultValue = "") String channels

    ) {
        List<VariantSaleResponse> products = variantService.getAllVariantsFilter(page, size, query, categoryIds, startDate, endDate, sortBy, order, channels);
        long totalItems = variantService.countVariantWebPage(query, categoryIds, startDate, endDate, channels);
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


    @GetMapping("base-products/variants/search")
    public ResponseEntity<ResponseObject> getAllVariantsByKeyword(@RequestParam(name = "keyword") String keyword) {

        List<VariantDto> variantDtos = variantService.getAllVariantsByKeyword(keyword);
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(variantDtos)
                .build());
    }


}
