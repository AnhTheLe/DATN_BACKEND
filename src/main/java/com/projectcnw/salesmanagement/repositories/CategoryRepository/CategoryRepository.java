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

    boolean existsByTitle(String name);

    boolean existsBySlug(String slug);

    boolean existsById(Integer id);
}
