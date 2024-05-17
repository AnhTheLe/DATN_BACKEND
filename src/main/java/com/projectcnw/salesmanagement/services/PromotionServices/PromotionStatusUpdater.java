package com.projectcnw.salesmanagement.services.PromotionServices;

import com.projectcnw.salesmanagement.models.Promotion;
import com.projectcnw.salesmanagement.models.enums.PromotionStatusType;
import com.projectcnw.salesmanagement.repositories.PromotionRepository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PromotionStatusUpdater {

    private static final Logger logger = LoggerFactory.getLogger(PromotionStatusUpdater.class);

    private final PromotionRepository promotionRepository;

    @Scheduled(fixedRate = 60000) // chạy mỗi 60 giây
    public void updatePromotionStatuses() {
        logger.info("Starting promotion status update task");

        Date now = new Date();
        List<Promotion> promotions = promotionRepository.findAll();

        for (Promotion promotion : promotions) {
            PromotionStatusType oldStatus = promotion.getStatus();
            if (promotion.getStartDate().after(now)) {
                promotion.setStatus(PromotionStatusType.scheduled);
            } else if (promotion.getEndDate().before(now)) {
                promotion.setStatus(PromotionStatusType.expired);
            } else {
                promotion.setStatus(PromotionStatusType.active);
            }

            if(oldStatus == null) {
                logger.info("Promotion {} status changed from {} to {}", promotion.getTitle(), "null", promotion.getStatus());
            }

            if (!oldStatus.equals(promotion.getStatus())) {
                logger.info("Promotion {} status changed from {} to {}", promotion.getTitle(), oldStatus, promotion.getStatus());
            }

            promotionRepository.save(promotion);
        }

        logger.info("Promotion status update task completed");
    }
}
