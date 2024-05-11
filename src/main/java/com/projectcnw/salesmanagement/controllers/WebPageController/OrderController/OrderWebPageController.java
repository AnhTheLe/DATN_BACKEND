package com.projectcnw.salesmanagement.controllers.WebPageController.OrderController;


import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.orderDtos.createOrder.CreateOrderDto;
import com.projectcnw.salesmanagement.services.OrderServices.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderWebPageController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseObject> createOrder(@RequestBody @Valid CreateOrderDto createOrderDto, @AuthenticationPrincipal UserDetails userDetails) {
        String staffPhone = userDetails.getUsername();
        orderService.createOrderWebPage(createOrderDto, staffPhone);
        return ResponseEntity.ok(ResponseObject.builder()
                .message("Tạo đơn hàng thành công")
                .responseCode(200)
                .build());
    }
}
