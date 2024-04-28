package com.projectcnw.salesmanagement.services.ShoppingCartService;

import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.shopping_cart.ShoppingCartRequest;
import com.projectcnw.salesmanagement.models.Customer;
import com.projectcnw.salesmanagement.models.Products.Variant;
import com.projectcnw.salesmanagement.models.ShoppingCart;
import com.projectcnw.salesmanagement.repositories.CustomerRepositories.CustomerRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.BaseProductRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.VariantRepository;
import com.projectcnw.salesmanagement.repositories.ShoppingCartRepository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final BaseProductRepository productRepository;
    private final VariantRepository variantRepository;
    private final CustomerRepository customerRepository;


    public ResponseEntity<ResponseObject> addToCart(Integer customerId, ShoppingCartRequest item) {
        Optional<Customer> customer = customerRepository.findCustomerById(customerId);
        if (customer.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Customer is not exists")
                    .build());
        }
        Optional<Variant> variant = variantRepository.findById(item.getProductId());
        if (variant.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Variant is not exists")
                    .build());
        }
        if (item.getQuantity() < 1 || variant.get().getQuantity() < item.getQuantity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Invalid product quantity or insufficient product quantity")
                    .build());
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByCustomer_IdAndVariant_Id(customerId, item.getProductId());
        if (shoppingCart == null || shoppingCart.getId() < 1) {
            ShoppingCart newProduct = ShoppingCart.builder()
                    .customer(customer.get())
                    .variant(variant.get())
                    .quantity(item.getQuantity())
                    .build();
            shoppingCartRepository.save(newProduct);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                    .responseCode(200)
                    .message("Add to cart successful")
                    .data(newProduct)
                    .build());
        } else {
            shoppingCart.setQuantity(shoppingCart.getQuantity() + item.getQuantity());
            return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                    .responseCode(200)
                    .message("Your cart is updated")
                    .build());
        }
    }

    public ResponseEntity<ResponseObject> updateCart(Integer customerId, ShoppingCartRequest item) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByCustomer_IdAndVariant_Id(customerId, item.getProductId());
        if (shoppingCart == null || shoppingCart.getId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(101)
                    .message("Update cart fail")
                    .data(null)
                    .build());
        }
        if (item.getQuantity() == 0) {
            shoppingCartRepository.delete(shoppingCart);
        } else {
            shoppingCart.setQuantity(item.getQuantity());
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .responseCode(200)
                .message("Update cart success")
                .data(shoppingCartRepository.save(shoppingCart))
                .build());
    }

    public ResponseEntity<ResponseObject> deleteItem(Integer customerId, Integer productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByCustomer_IdAndVariant_Id(customerId, productId);
        if (shoppingCart == null || shoppingCart.getId() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(101)
                    .message("cannot find this item in your cart")
                    .data(null)
                    .build());
        }
        shoppingCartRepository.delete(shoppingCart);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .responseCode(200)
                .message("Delete item success")
                .data(shoppingCart)
                .build());
    }

    public ResponseEntity<ResponseObject> getAllShoppingCart(Integer customerId) {
        Iterable<Map<String, Object>> shoppingCart = shoppingCartRepository.findAllByUserInfo_Id(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.builder()
                .responseCode(200)
                .message("Success")
                .data(shoppingCart)
                .build());
    }
}