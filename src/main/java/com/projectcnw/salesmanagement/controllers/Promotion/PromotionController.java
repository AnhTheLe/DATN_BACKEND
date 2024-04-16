package com.projectcnw.salesmanagement.controllers.Promotion;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.promotion.PromotionRequest;
import com.projectcnw.salesmanagement.services.PromotionServices.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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


}
