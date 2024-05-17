package com.projectcnw.salesmanagement.repositories.CategoryRepository;

import com.projectcnw.salesmanagement.models.Products.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    //get list Category by list ids
    @Query(value = "select * from category where id in :ids", nativeQuery = true)
    List<Category> getListCategoryByIds(@Param("ids") List<Integer> ids);

    //get list Category by product id
    @Query(value = "SELECT c.id\n" +
            "FROM category c INNER JOIN product_category pc ON c.id = pc.category_id WHERE pc.product_id = :productId"
            , nativeQuery = true)
    List<Integer> getListCategoryIdByProductId(@Param("productId") int productId);

    //get list Category by product id
    @Query(value = "SELECT c.*\n" +
            "FROM category c INNER JOIN product_category pc ON c.id = pc.category_id WHERE pc.product_id = :productId"
            , nativeQuery = true)
    List<Category> getListCategoryByProductId(@Param("productId") int productId);

    boolean existsByTitle(String name);

    boolean existsBySlug(String slug);

    boolean existsById(Integer id);
}
