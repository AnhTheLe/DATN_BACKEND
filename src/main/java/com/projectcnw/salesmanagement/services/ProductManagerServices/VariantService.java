
package com.projectcnw.salesmanagement.services.ProductManagerServices;

import com.projectcnw.salesmanagement.dto.orderDtos.TopOrder;
import com.projectcnw.salesmanagement.dto.productDtos.*;
import com.projectcnw.salesmanagement.dto.promotion.PromotionResponse;
import com.projectcnw.salesmanagement.exceptions.NotFoundException;
import com.projectcnw.salesmanagement.exceptions.ProductManagerExceptions.ProductException;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Variant;
import com.projectcnw.salesmanagement.models.Promotion;
import com.projectcnw.salesmanagement.models.enums.PromotionEnumType;
import com.projectcnw.salesmanagement.repositories.OrderRepositories.OrderRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.BaseProductRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.NonJPARepository.impl.NonJpaVariantRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.VariantRepository;
import com.projectcnw.salesmanagement.repositories.PromotionRepository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VariantService {

    private final VariantRepository variantRepository;

    private final BaseProductRepository baseProductRepository;

    private final PromotionRepository promotionRepository;

    private final NonJpaVariantRepository nonJpaVariantRepository;

    private final OrderRepository orderRepository;
    private ModelMapper modelMapper = new ModelMapper();

    public List<VariantSaleResponse> getAllVariants(int page, int size) {
        int offset = (page - 1) * size;
        List<Variant> iVariantDtos = variantRepository.findAllVariant(size, offset);
        return getListVariantResponseFromVariant(iVariantDtos);
    }

    public List<VariantSaleResponse> getAllVariantsFilter(int page, int size, String query, String categoryIds, String start_date, String end_date) {
        List<Integer> categoryIdList = categoryIds == null || categoryIds.isEmpty() ? null : Arrays.stream(categoryIds.split(",")).map(Integer::parseInt).toList();
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (start_date != null && !start_date.isEmpty()) {
            try {
                startDate = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }

        if (end_date != null && !end_date.isEmpty()) {
            try {
                endDate = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX);
                ;
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }
        List<Variant> iVariantDtos = nonJpaVariantRepository.getAllVariantsFilter(page, size, query, categoryIdList, startDate, endDate);
        return getListVariantResponseFromVariant(iVariantDtos);
    }

    private VariantSaleResponse getVariantWithBestPromotion(Variant variant) {
        VariantSaleResponse variantSaleResponse = new VariantSaleResponse();
        Promotion bestPromotion = null;
        List<Promotion> promotions = promotionRepository.getAllPromotionByVariantId(variant.getId());
        log.info("Promotion: {}", promotions);
        // choose promotion which variant price after discount is lowest
        if (promotions != null && !promotions.isEmpty()) {
            double minPrice = Double.MAX_VALUE;
            for (Promotion promotion : promotions) {
                double discountedPrice = calculateDiscountedPrice(variant, promotion);
                if (discountedPrice < minPrice) {
                    minPrice = discountedPrice;
                    bestPromotion = promotion;
                }
            }
        }
        double discountPrice = bestPromotion != null ? bestPromotion.getValueType().name().equals(PromotionEnumType.PERCENTAGE.name()) ? (double) variant.getRetailPrice() * bestPromotion.getValue() / 100 : bestPromotion.getValue() : 0;
        variantSaleResponse.setPromotion(bestPromotion != null ? modelMapper.map(bestPromotion, PromotionResponse.class) : null);
        variantSaleResponse.setName(variant.getName());
        variantSaleResponse.setSku(variant.getSku());
        variantSaleResponse.setWholeSalePrice(variant.getWholeSalePrice());
        variantSaleResponse.setBaseId(variant.getBaseProduct().getId());
        variantSaleResponse.setBarcode(variant.getBarcode());
        variantSaleResponse.setRetailPrice(variant.getRetailPrice());
        variantSaleResponse.setQuantity(variant.getQuantity());
        variantSaleResponse.setImportPrice(variant.getImportPrice());
        variantSaleResponse.setValue1(variant.getValue1());
        variantSaleResponse.setValue2(variant.getValue2());
        variantSaleResponse.setValue3(variant.getValue3());
        variantSaleResponse.setDiscount((int) discountPrice);
        variantSaleResponse.setDiscountedPrice((int) (variant.getRetailPrice() - discountPrice));
        variantSaleResponse.setId(variant.getId());
        variantSaleResponse.setImage(variant.getImage());
        return variantSaleResponse;
    }

    private List<VariantSaleResponse> getListVariantResponseFromVariant(List<Variant> iVariantDtos) {
        double minPrice = Double.MAX_VALUE;
        List<VariantSaleResponse> variantSaleResponses = new ArrayList<>();
        for (Variant variant : iVariantDtos) {
            VariantSaleResponse variantSaleResponse = getVariantWithBestPromotion(variant);
            variantSaleResponses.add(variantSaleResponse);
        }
        return variantSaleResponses;
    }

    public List<VariantSaleResponse> getTop10VariantHasPromotion() {
        List<Variant> listTopVariantSale = variantRepository.getTop10VariantHasPromotion();
        return getListVariantResponseFromVariant(listTopVariantSale);
    }

//    public List<VariantSaleResponse> getTopSaleVariant() {
//        List<Variant> listTopVariantSale = variantRepository.getTopSaleVariant();
//        return getListVariantResponseFromVariant(listTopVariantSale);
//    }

    public List<TopSaleVariant> getTopSaleVariant(java.util.Date startDate, java.util.Date endDate, String type) {
        List<TopSaleVariant> result = new ArrayList<>();
        List<Object[]> listVariant = orderRepository.topProductByRevenue(startDate, endDate);
        if ("revenue".equals(type)) {
            listVariant = orderRepository.topProductByRevenue(startDate, endDate);
            System.out.println("revenue");
        } else if ("quantity".equals(type)) {
            listVariant = orderRepository.topProductByQuantity(startDate, endDate);
            System.out.println("quantity");
        } else if ("order".equals(type)) {
            listVariant = orderRepository.topProductByOrder(startDate, endDate);
            System.out.println("order");
        }

        for (Object[] data : listVariant) {
            int variantId = (int) data[0];
            Variant variant = null;
            variant = variantRepository.findById((Integer) variantId)
                    .orElseThrow(() -> new NotFoundException("variant " + variantId + " not found"));

            VariantSaleResponse variantSaleResponse = getVariantWithBestPromotion(variant);

            BigDecimal value = BigDecimal.valueOf(((Number) data[1]).doubleValue());
            TopSaleVariant topOrder = new TopSaleVariant(variantSaleResponse, value);
            result.add(topOrder);
        }

        return result;
    }


    private double calculateDiscountedPrice(Variant variant, Promotion promotion) {
        // Áp dụng promotion để tính toán giá sau khi giảm giá
        double discountedPrice = variant.getRetailPrice() - (promotion.getValueType().name().equals(PromotionEnumType.PERCENTAGE.name()) ? (double) variant.getRetailPrice() * promotion.getValue() / 100 : promotion.getValue());
        ;
        // Giả sử các phương thức khác để tính giá sau khi áp dụng promotion
        return discountedPrice;
    }

    public long countVariantWebPage(String query, String categoryIds, String start_date, String end_date) {

        List<Integer> categoryIdList = categoryIds == null || categoryIds.equals("") ? null : Arrays.asList(categoryIds.split(",")).stream().map(Integer::parseInt).toList();
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (start_date != null && !start_date.isEmpty()) {
            try {
                startDate = LocalDate.parse(start_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }

        if (end_date != null && !end_date.isEmpty()) {
            try {
                endDate = LocalDate.parse(end_date, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atTime(LocalTime.MAX);
                ;
            } catch (DateTimeParseException e) {
                log.error("Không thể chuyển đổi start_date thành LocalDateTime", e);
            }
        }
        return nonJpaVariantRepository.countVariant(query, categoryIdList, startDate, endDate);
    }

    public long countVariant() {
        return variantRepository.count();
    }

    public VariantDto createVariant(int baseId, VariantDto variantDto) {
        BaseProduct baseProduct = baseProductRepository.findById(baseId);
        if (baseProduct == null) throw new ProductException("baseProduct is not found");
        ILastIdVariant lastIdVariant = variantRepository.getLastSetId();

        Variant variant = modelMapper.map(variantDto, Variant.class);
        if (variant.getSku() == null || variant.getSku().equals("")) {
            int tmpSku = baseProduct.getId() + 100000 + lastIdVariant.getLastId() + 1;
            String skuString = "SKU" + baseProduct.getId() + tmpSku;
            variant.setSku(skuString);
        } else if (variant.getSku().startsWith("SKU")) {
            throw new ProductException("Mã SKU không được bắt đầu bằng \"SKU\"");
        }
        if (variant.getBarcode() == null || variant.getBarcode().equals("")) {
            variant.setBarcode(variant.getSku());
        }
        if (baseProduct.getAttribute1() != null && !baseProduct.getAttribute1().isBlank())
            if (variant.getValue1() == null || variant.getValue1().isBlank())
                throw new ProductException("Thuộc tính " + baseProduct.getAttribute1() + " không được để trống");
        if (baseProduct.getAttribute2() != null && !baseProduct.getAttribute2().isBlank())
            if (variant.getValue2() == null || variant.getValue2().isBlank())
                throw new ProductException("Thuộc tính " + baseProduct.getAttribute2() + " không được để trống");
        if (baseProduct.getAttribute3() != null && !baseProduct.getAttribute3().isBlank())
            if (variant.getValue3() == null || variant.getValue3().isBlank())
                throw new ProductException("Thuộc tính " + baseProduct.getAttribute3() + " không được để trống");
        variant.setBaseProduct(baseProduct);
        Variant variant1 = variantRepository.save(variant);


        return modelMapper.map(variant1, VariantDto.class);
    }

    public VariantDto updateVariant(int baseId, VariantDto variantDto) {
        BaseProduct baseProduct = baseProductRepository.findById(baseId);
        if (baseProduct == null) throw new ProductException("Không tìm thấy sản phẩm");

        Variant variant = variantRepository.findById(variantDto.getId());
        if (variant == null) throw new ProductException("Không tìm thấy phiên bản");

        Variant variant1 = modelMapper.map(variantDto, Variant.class);
        variant1.setCreatedAt(variant.getCreatedAt());
        variant1.setBaseProduct(baseProduct);
        variant1.setQuantity(variant1.getQuantity());
        if (variant1.getSku() == null || variant1.getSku().equals("")) {
            int tmpSku = baseProduct.getId() + 100000 + variant.getId();
            String skuString = "SKU" + baseProduct.getId() + tmpSku;
            variant1.setSku(skuString);
        }
        if (variant1.getBarcode() == null || variant1.getBarcode().equals("")) {
            variant1.setBarcode(variant1.getSku());
        }
        if (baseProduct.getAttribute2() != null)
            if (variant1.getValue1() == null || variant1.getValue1().isBlank())
                throw new ProductException("Thuộc tính" + baseProduct.getAttribute1() + " không được trống");
        if (baseProduct.getAttribute2() != null && !baseProduct.getAttribute2().equals(""))
            if (variant1.getValue2() == null || variant1.getValue2().isBlank())
                throw new ProductException("Thuộc tính" + baseProduct.getAttribute2() + " không được trống");
        if (baseProduct.getAttribute3() != null && !baseProduct.getAttribute3().equals(""))
            if (variant1.getValue3() == null || variant1.getValue3().isBlank())
                throw new ProductException("Thuộc tính" + baseProduct.getAttribute3() + " không được trống");


        Variant variant2 = variantRepository.save(variant1);

        return modelMapper.map(variant2, VariantDto.class);
    }

    @Transactional
    public void deleteVariantById(int baseId, int variantId) {
        IBaseProductDto iBaseProductDto = baseProductRepository.findBaseProductById(baseId);
        if (iBaseProductDto == null) throw new ProductException("baseProduct is not found");

        if (iBaseProductDto.getVariantNumber() == 1)
            throw new ProductException("It is not possible to delete a single version of a product");
        Variant variant = variantRepository.findById(variantId);
        if (variant == null || variant.isDeleted()) throw new ProductException("variant is not found");

        variantRepository.deleteVariantById(variantId);
    }

    @Transactional
    public List<VariantDto> getAllVariantsByKeyword(String keyword) {
        List<IVariantDto> iVariantDtos = variantRepository.findAllVariantsByKeyword(keyword);
        return Arrays.asList(modelMapper.map(iVariantDtos, VariantDto[].class));
    }

}
