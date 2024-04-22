package com.projectcnw.salesmanagement.controllers.WebPageController.ShoppingCartController;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.shopping_cart.ShoppingCartRequest;
import com.projectcnw.salesmanagement.services.CustomerServices.CustomerAuth.CustomerDetailService;
import com.projectcnw.salesmanagement.services.ShoppingCartService.ShoppingCartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.projectcnw.salesmanagement.utils.UserUtil.getCurrentCustomerId;

@RestController
@RequestMapping("/api/user/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    private final CustomerDetailService userDetailsService;

     @PostMapping("/add")
     public ResponseEntity<ResponseObject> addToCart( @RequestBody ShoppingCartRequest item) {
         Integer customerId = getCurrentCustomerId();
         return ResponseEntity.ok(shoppingCartService.addToCart(customerId, item).getBody());
     }
}
