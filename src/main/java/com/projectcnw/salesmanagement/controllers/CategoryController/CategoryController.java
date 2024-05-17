package com.projectcnw.salesmanagement.controllers.CategoryController;


import com.projectcnw.salesmanagement.controllers.BaseController;
import com.projectcnw.salesmanagement.dto.Category.CategoryRequest;
import com.projectcnw.salesmanagement.dto.PagedResponseObject;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.services.CategoryServices.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<PagedResponseObject> getAll(
            @RequestParam(defaultValue = "", name = "query") String title,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "pageSize") int size,
            @RequestParam(defaultValue = "", name = "startDate") String startDate,
            @RequestParam(defaultValue = "", name = "endDate") String endDate
    ) {
        return categoryService.getAllCategory(page, size, startDate, endDate, title);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getCategoryById(@PathVariable Integer id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        return categoryService.createCategory(categoryRequest);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseObject> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryRequest categoryRequest){
        return categoryService.updateCategory(id, categoryRequest);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseObject> deleteCategory(@PathVariable Integer id){
        return categoryService.deleteCategory(id);
    }

    @PostMapping("/add-products/{id}")
    public ResponseEntity<ResponseObject> addProduct(@PathVariable Integer id, @RequestBody List<Integer> productIds){
        return categoryService.addListProductToCategory(id, productIds);
    }

    @DeleteMapping("/delete-products/{id}")
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable Integer id, @RequestBody List<Integer> productIds){
        return categoryService.deleteListProductFromCategory(id, productIds);
    }
}
