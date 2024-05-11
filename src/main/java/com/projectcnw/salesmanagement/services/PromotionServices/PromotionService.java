package com.projectcnw.salesmanagement.services.PromotionServices;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.promotion.PromotionRequest;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Category;
import com.projectcnw.salesmanagement.models.Promotion;
import com.projectcnw.salesmanagement.models.enums.PromotionPolicyApplyType;
import com.projectcnw.salesmanagement.repositories.CategoryRepository.CategoryRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.BaseProductRepository;
import com.projectcnw.salesmanagement.repositories.PromotionRepository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final CategoryRepository categoryRepository;
    private final BaseProductRepository baseProductRepository;

    @Transactional
    public ResponseEntity<ResponseObject> createPromotion(PromotionRequest promotion) {
        // Check if endDate is null, set it to a future date if it is null
        Instant futureDate = LocalDate.now().plusYears(10).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date endDate = promotion.getEndDate() != null ? promotion.getEndDate() : Date.from(futureDate);
        List<Category> categories = new ArrayList<>();
        List<BaseProduct> products = new ArrayList<>();

        if (promotion.getPolicyApply() == PromotionPolicyApplyType.CATEGORY && promotion.getCategoryIds() != null) {
            categories = categoryRepository.getListCategoryByIds(promotion.getCategoryIds());
            products = baseProductRepository.getListBaseProductByCategoryIds(promotion.getCategoryIds());
        }
        if (promotion.getPolicyApply() == PromotionPolicyApplyType.PRODUCT && promotion.getProductIds() != null) {
            products = baseProductRepository.getListBaseProductByIds(promotion.getProductIds());
        }
        if(promotion.getTitle() == null) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .responseCode(400).message("Title are required").build());
        }
        if(promotion.getValue() == null) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .responseCode(400).message("Value are required").build());
        }
        if(promotion.getValueType() == null) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .responseCode(400).message("Value type are required").build());
        }
        if(promotion.getPolicyApply() == null) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .responseCode(400).message("Policy apply are required").build());
        }

        //check nếu ngày bắt đầu lớn hơn ngày hôm nay thì set active = false
        if(promotion.getStartDate() != null && promotion.getStartDate().after(Date.from(Instant.now()))){
            promotion.setActive(false);
        }else {
            promotion.setActive(true);
        }

        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200).message("")
                .data(promotionRepository.save(Promotion.builder()
                        .title(promotion.getTitle())
                        .value(promotion.getValue())
                        .startDate(promotion.getStartDate() != null ? promotion.getStartDate() : Date.from(Instant.now()))
                        .endDate(endDate)
                        .valueType(promotion.getValueType())
                        .policyApply(promotion.getPolicyApply())
                        .description(promotion.getDescription())
                        .active(promotion.isActive())
                        .categories(categories)
                        .products(products)
                        .build())).build());
    }

    @Transactional
    public ResponseEntity<ResponseObject> updatePromotion(Integer id, PromotionRequest promotion) {
        Promotion promotionUpdate = promotionRepository.findById(id).orElse(null);
        if (promotionUpdate == null) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .responseCode(400).message("Promotion not found").build());
        }
        List<Category> categories = new ArrayList<>();
        List<BaseProduct> products = new ArrayList<>();

        if (promotion.getPolicyApply() == PromotionPolicyApplyType.CATEGORY && promotion.getCategoryIds() != null) {
            categories = categoryRepository.getListCategoryByIds(promotion.getCategoryIds());
        }
        if (promotion.getPolicyApply() == PromotionPolicyApplyType.PRODUCT && promotion.getProductIds() != null) {
            products = baseProductRepository.getListBaseProductByIds(promotion.getProductIds());
        }
        promotionUpdate.setTitle(promotion.getTitle());
        promotionUpdate.setValue(promotion.getValue());
        promotionUpdate.setStartDate(promotion.getStartDate());
        promotionUpdate.setEndDate(promotion.getEndDate());
        promotionUpdate.setActive(promotion.isActive());
        promotionUpdate.setValueType(promotion.getValueType());
        promotionUpdate.setPolicyApply(promotion.getPolicyApply());
        promotionUpdate.setDescription(promotion.getDescription());
        promotionUpdate.setCategories(categories);
        promotionUpdate.setProducts(products);

        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200).message("Update promotion success")
                .data(promotionRepository.save(promotionUpdate)).build());
    }

    @Transactional
    public ResponseEntity<ResponseObject> deletePromotion(Integer id) {
        Promotion promotion = promotionRepository.findById(id).orElse(null);
        if (promotion == null) {
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .responseCode(400).message("Promotion not found").build());
        }
        promotionRepository.delete(promotion);
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200).message("Delete promotion success").build());
    }

    @Transactional
    public ResponseEntity<ResponseObject> deleteListPromotion(List<Integer> ids) {
        List<Promotion> promotions = new ArrayList<>();
        for (Integer id : ids) {
            Promotion promotion = promotionRepository.findById(id).orElse(null);
            if (promotion != null) {
                promotions.add(promotion);
            } else {
                return ResponseEntity.badRequest().body(ResponseObject.builder()
                        .responseCode(400).message("Promotion not found").build());
            }
        }
        promotionRepository.deleteAll(promotions);
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200).message("Delete promotion success").build());
    }

    public ResponseEntity<ResponseObject> getCouponPromotion (String title) {
        log.info("title: {}", title);
        List<Promotion> promotions = promotionRepository.getPromotionsByTitleAndPolicyApplyAndActive(title, PromotionPolicyApplyType.COUPON.name());
        if(promotions.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(
                    ResponseObject.builder()
                            .responseCode(422)
                            .message("Không tìm thấy mã coupon hợp lệ")
                            .data(null)
                            .build()
            );
        }
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .responseCode(200)
                        .message("Áp dụng mã giảm giá thành công")
                        .data(promotions.get(0))
                        .build());
    }


}
