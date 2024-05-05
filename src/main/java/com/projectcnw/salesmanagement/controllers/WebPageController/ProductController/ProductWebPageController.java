package com.projectcnw.salesmanagement.controllers.WebPageController.ProductController;




import com.projectcnw.salesmanagement.dto.PagedResponseObject;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.dto.productDtos.TopSaleVariant;
import com.projectcnw.salesmanagement.dto.productDtos.VariantSaleResponse;
import com.projectcnw.salesmanagement.services.OrderServices.OrderService;
import com.projectcnw.salesmanagement.services.ProductManagerServices.BaseProductService;
import com.projectcnw.salesmanagement.services.ProductManagerServices.VariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductWebPageController {

    private final VariantService variantService;
    private final BaseProductService productService;
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProductDetail(@PathVariable int id) {
        return productService.getProductSaleResponse(id);
    }

}
