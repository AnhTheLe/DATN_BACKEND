package com.projectcnw.salesmanagement.controllers.Promotion;

import com.projectcnw.salesmanagement.dto.PagedResponseObject;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.promotion.PromotionRequest;
import com.projectcnw.salesmanagement.models.Promotion;
import com.projectcnw.salesmanagement.services.PromotionServices.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/promotion")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @PostMapping("")
    public ResponseEntity<ResponseObject> createPromotion(@RequestBody PromotionRequest promotion) {
        return promotionService.createPromotion(promotion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updatePromotion(@PathVariable("id") int id, @RequestBody PromotionRequest promotion) {
        return promotionService.updatePromotion(id, promotion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deletePromotion(@PathVariable("id") int id) {
        return promotionService.deletePromotion(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deletePromotions(@RequestBody List<Integer> ids) {
        return promotionService.deleteListPromotion(ids);
    }

    @GetMapping("")
    public ResponseEntity<PagedResponseObject> getAllPromotion(
            @RequestParam(defaultValue = "", name = "query") String query,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "20", name = "pageSize") int size,
            @RequestParam(defaultValue = "", name = "startDate") String startDate,
            @RequestParam(defaultValue = "", name = "status") String status,
            @RequestParam(defaultValue = "id", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "desc", name = "order") String order,
            @RequestParam(defaultValue = "", name = "policyApply") String policyApply

    ) {
        List<Promotion> promotions = promotionService.getAllPromotionByFilter(page, size, query, policyApply, status, startDate, sortBy, order);
        long totalItems = promotionService.countPromotion(query, policyApply, status, startDate, sortBy, order);
        int totalPages = (int) Math.ceil((double) totalItems / size);
        return ResponseEntity.ok(PagedResponseObject.builder()
                .page(page)
                .perPage(size)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .responseCode(200)
                .message("Success")
                .data(promotions)
                .build());
    }

    @GetMapping("/coupon")
    public ResponseEntity<ResponseObject> getAllCouponPromotion(
            @RequestParam(defaultValue = "", name = "title") String title
    ) {
        return promotionService.getCouponPromotion(title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getPromotionById(@PathVariable("id") int id) {
        return promotionService.getPromotionById(id);
    }


}
