package com.projectcnw.salesmanagement.repositories.ProductManagerRepository;


import com.projectcnw.salesmanagement.dto.productDtos.ILastIdVariant;
import com.projectcnw.salesmanagement.dto.productDtos.IVariantDto;
import com.projectcnw.salesmanagement.models.Products.Variant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

public interface VariantRepository extends JpaRepository<Variant, Integer> {
    //Lấy ra danh sách tất cả các Variant
    @Query(value = "SELECT v.* from variant v \n" +
            "WHERE v.is_deleted = false ORDER BY v.created_at DESC LIMIT :size OFFSET :offset"
            , nativeQuery = true)
    List<Variant> findAllVariant(@Param("size") int size, @Param("offset") int offset);

    // get all promotion by variantId

    @Query(value = "SELECT DISTINCT v.id as id, v.barcode as barcode, v.image as image, v.import_price as importPrice, v.name as name, v.quantity as quantity, v.retail_price as retailPrice, v.sku as sku, v.wholesale_price as wholeSalePrice, v.created_at as createdAt, v.updated_at as updatedAt, v.base_id as baseId, v.value1 as value1, v.value2 as value2, v.value3 as value3\n" +
            "FROM variant v WHERE v.is_deleted = false and (v.sku LIKE CONCAT('%', :keyword, '%') OR v.name LIKE CONCAT('%', :keyword, '%') OR v.barcode LIKE CONCAT('%', :keyword, '%')) ORDER BY v.created_at DESC"
            , nativeQuery = true)
    List<IVariantDto> findAllVariantsByKeyword(@Param("keyword") String keyword);
//    @Transactional
//    @Modifying
    @Query(value = "SELECT COUNT(*) FROM variant v WHERE v.is_deleted = false", nativeQuery = true)
    long count();

    @Query(value = "SELECT v.*" +
            " FROM variant v WHERE v.base_id = :baseId AND v.is_deleted = false"
            , nativeQuery = true)
    List<Variant> findVariantsByBaseProductId(@Param("baseId") int baseId);

    //getTop10VariantHasPromotion()
    @Query(value = "SELECT v.*\n" +
            "FROM variant v INNER JOIN promotion_product pp ON v.base_id = pp.product_id INNER JOIN promotion p ON pp.promotion_id = p.id WHERE v.is_deleted = false ORDER BY p.created_at DESC LIMIT 10"
            , nativeQuery = true)
    List<Variant> getTop10VariantHasPromotion();

    // find list variant by list category id
    @Query(value = "SELECT v.*\n" +
            "FROM variant v INNER JOIN base_product bp ON v.base_id = bp.id INNER JOIN product_category pc ON bp.id = pc.product_id WHERE pc.category_id IN :categoryIds AND v.is_deleted = false AND bp.is_deleted = false ORDER BY v.created_at DESC LIMIT 10"
            , nativeQuery = true)
    List<Variant> findVariantsByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

    Variant findById(int variantId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE variant SET quantity = :realQuantity WHERE id =:variantId", nativeQuery = true)
    void updateQuantityAfterBalance(@Param("variantId") int variantId, @Param("realQuantity") int realQuantity);

    @Transactional
    @Modifying
    @Query(value = "UPDATE variant SET is_deleted = true WHERE id =:variantId", nativeQuery = true)
    void deleteVariantById(@Param("variantId") int variantId);

    @Query(value = "SELECT MAX(v.id) as lastId FROM variant v", nativeQuery = true)
    ILastIdVariant getLastSetId();

}
