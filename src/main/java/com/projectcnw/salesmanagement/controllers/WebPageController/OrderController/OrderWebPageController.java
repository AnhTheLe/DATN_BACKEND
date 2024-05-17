package com.projectcnw.salesmanagement.controllers.WebPageController.OrderController;


import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.orderDtos.OrderDetailInfo;
import com.projectcnw.salesmanagement.dto.orderDtos.createOrder.CreateOrderDto;
import com.projectcnw.salesmanagement.services.OrderServices.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static com.projectcnw.salesmanagement.utils.UserUtil.getCurrentCustomerId;

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
    @GetMapping
    public ResponseEntity<ResponseObject> getOrderList() {
        Integer customerId = getCurrentCustomerId();
        if(customerId == null) {
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Không tìm thấy thông tin khách hàng")
                    .responseCode(404)
                    .build());
        }
        return orderService.getAllOrderByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrderDetailInfo(@PathVariable @Valid int id) {
        return orderService.getOrderDetail(id);
    }
}
