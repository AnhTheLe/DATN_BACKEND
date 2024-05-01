package com.projectcnw.salesmanagement.repositories.ShoppingCartRepository;

import com.projectcnw.salesmanagement.models.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    @Query("SELECT u.id as idCart, u.variant as variant, u.quantity as quantity FROM ShoppingCart u WHERE u.customer.id=:userId")
    Iterable<Map<String,Object>> findAllByUserInfo_Id(@Param("userId") Integer id);


    ShoppingCart findShoppingCartByCustomer_IdAndVariant_Id(Integer customer_id, Integer variant_id);

    List<ShoppingCart> findAllByCustomer_IdAndVariant_IdIn(Integer customer_id, List<Integer> variant_id);

}
