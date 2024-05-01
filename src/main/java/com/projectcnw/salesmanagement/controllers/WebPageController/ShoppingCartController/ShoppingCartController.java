package com.projectcnw.salesmanagement.controllers.WebPageController.ShoppingCartController;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.shopping_cart.ShoppingCartRequest;
import com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth.CustomerDetailService;
import com.projectcnw.salesmanagement.services.ShoppingCartService.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.projectcnw.salesmanagement.utils.UserUtil.getCurrentCustomerId;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    private final CustomerDetailService userDetailsService;

    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addToCart(@RequestBody ShoppingCartRequest item) {
        Integer customerId = getCurrentCustomerId();
        return ResponseEntity.ok(shoppingCartService.addToCart(customerId, item).getBody());
    }

    @GetMapping
    public ResponseEntity<ResponseObject> getCart() {
        Integer customerId = getCurrentCustomerId();
        return ResponseEntity.ok(shoppingCartService.getAllShoppingCart(customerId).getBody());
    }

    @PutMapping("/update-purchase")
    public ResponseEntity<ResponseObject> updatePurchase(@RequestBody ShoppingCartRequest item) {
        Integer customerId = getCurrentCustomerId();
        return ResponseEntity.ok(shoppingCartService.updateCart(customerId, item).getBody());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseObject> deleteCart(@RequestBody List<Integer> variantIds) {
        Integer customerId = getCurrentCustomerId();
        return ResponseEntity.ok(shoppingCartService.deleteListItem(variantIds, customerId).getBody());
    }
}
