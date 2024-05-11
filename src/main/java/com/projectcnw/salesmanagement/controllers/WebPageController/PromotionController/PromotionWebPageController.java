package com.projectcnw.salesmanagement.controllers.WebPageController.PromotionController;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.services.PromotionServices.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotion")
@RequiredArgsConstructor
public class PromotionWebPageController {
    private final PromotionService promotionService;
    @GetMapping("/coupon")
    public ResponseEntity<ResponseObject> getAllCouponPromotion(
            @RequestParam(defaultValue = "", name = "title") String title
    ) {
        return promotionService.getCouponPromotion(title);
    }
}
