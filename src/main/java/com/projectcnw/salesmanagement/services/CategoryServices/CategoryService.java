package com.projectcnw.salesmanagement.services.CategoryServices;

import com.projectcnw.salesmanagement.dto.Category.CategoryRequest;
import com.projectcnw.salesmanagement.dto.Category.CategoryResponse;
import com.projectcnw.salesmanagement.dto.PagedResponseObject;
import com.projectcnw.salesmanagement.dto.ResponseObject;
import com.projectcnw.salesmanagement.models.Products.BaseProduct;
import com.projectcnw.salesmanagement.models.Products.Category;
import com.projectcnw.salesmanagement.repositories.CategoryRepository.CategoryRepository;
import com.projectcnw.salesmanagement.repositories.ProductManagerRepository.BaseProductRepository;
import com.projectcnw.salesmanagement.repositories.spec.CategorySpecification;
import com.projectcnw.salesmanagement.repositories.spec.MySpecification;
import com.projectcnw.salesmanagement.repositories.spec.SearchCriteria;
import com.projectcnw.salesmanagement.repositories.spec.SearchOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BaseProductRepository productRepository;

    private final ModelMapper modelMapper;

    public ResponseEntity<PagedResponseObject> getAllCategory(int page, int size, String startDate, String endDate, String title) {

        Specification<Category> spec = where(null);
        Pageable paging = PageRequest.of(page, size, Sort.by(
                        Sort.Order.desc("createdAt")
                )
        );

        if (!startDate.isEmpty()) {
            LocalDate date = LocalDate.parse(startDate);
            MySpecification esFoodStartDate = new MySpecification();
            esFoodStartDate.add(new SearchCriteria("createdAt", date, SearchOperation.DATE_START));
            spec = spec.and(esFoodStartDate);
        }

        if (!endDate.isEmpty()) {
            LocalDate date = LocalDate.parse(endDate);
            MySpecification esFoodEndDate = new MySpecification();
            esFoodEndDate.add(new SearchCriteria("createdAt", date, SearchOperation.DATE_END));
            spec = spec.and(esFoodEndDate);
        }

        if (!title.isEmpty()) {
            spec = spec.and(CategorySpecification.hasNameLike(title));
        }
        Page<Category> listCategories = categoryRepository.findAll(spec, paging);

        List<Category> list = Arrays.asList(modelMapper.map(listCategories.getContent(), Category[].class));
        List<CategoryResponse> listResponse = new ArrayList<>();
        for (Category category : list) {
            listResponse.add(CategoryResponse.builder().
                    id(category.getId()).
                    title(category.getTitle()).
                    slug(category.getSlug()).
                    description(category.getDescription()).
                    metaTitle(category.getMetaTitle()).
                    productCount(productRepository.countProductByCategory(category.getId())).
                    build());
        }
        log.info("Get category by name use paging successfully");
        return ResponseEntity.ok(PagedResponseObject.builder()
                .responseCode(200)
                .page(listCategories.getNumber())
                .totalPages(listCategories.getTotalPages())
                .totalItems(listCategories.getTotalElements())
                .perPage(listCategories.getSize())
                .message("Get all category success")
                .data(listResponse)
                .build());
    }

    @Transactional
    public ResponseEntity<ResponseObject> createCategory(CategoryRequest category) {
        if (categoryRepository.existsByTitle(category.getTitle()) || categoryRepository.existsBySlug(category.getSlug())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Tên danh mục đã tồn tại")
                    .build());
        }
        List<BaseProduct> listProduct = new ArrayList<>();
        for (Integer productId : category.getProductIds()) {
            if (productRepository.existsById(productId)) {
                listProduct.add(productRepository.findById(productId).
                        orElseThrow(() -> new RuntimeException("Product not found")));
            }
        }
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Create category success")
                .data(categoryRepository.save(Category.builder()
                        .title(category.getTitle())
                        .slug(!(category.getSlug() == null || category.getSlug().isEmpty()) ? category.getSlug() : category.getTitle().toLowerCase().replace(" ", "-"))
                        .description(category.getDescription())
                        .products(listProduct)
                        .build()))
                .build());
    }

    @Transactional
    public ResponseEntity<ResponseObject> updateCategory(Integer id, CategoryRequest category) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Category not found")
                    .build());
        }

        Category categoryUpdate = categoryRepository.findById(id).get();
        categoryUpdate.setTitle(category.getTitle());
        categoryUpdate.setSlug(
                !(category.getSlug() == null || category.getSlug().isEmpty())
                        ? category.getSlug()
                        : category.getTitle().toLowerCase().replace(" ", "-")
        );
        categoryUpdate.setDescription(category.getDescription());
        categoryUpdate.setMetaTitle(category.getMetaTitle());
        List<BaseProduct> listProduct = new ArrayList<>();
        for (Integer productId : category.getProductIds()) {
            if (productRepository.existsById(productId)) {
                listProduct.add(productRepository.findById(productId).
                        orElseThrow(() -> new RuntimeException("Product not found")));
            }
        }
        categoryUpdate.setProducts(listProduct);

        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Update category success")
                .data(categoryRepository.save(categoryUpdate))
                .build());
    }

    @Transactional
    public ResponseEntity<ResponseObject> deleteCategory(Integer id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Category not found")
                    .build());
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Delete category success")
                .build());
    }

    @Transactional
    //add list product to category
    public ResponseEntity<ResponseObject> addListProductToCategory(Integer id, List<Integer> listProductId) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Category not found")
                    .build());
        }
        Category category = categoryRepository.findById(id).get();
        List<BaseProduct> listProduct = new ArrayList<>();
        for (Integer productId : listProductId) {
            if (productRepository.existsById(productId)) {
                listProduct.add(productRepository.findById(productId).
                        orElseThrow(() -> new RuntimeException("Product not found")));
            }
        }
        category.setProducts(listProduct);
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Add list product to category success")
                .data(categoryRepository.save(category))
                .build());
    }

    @Transactional
    // delete list product from category
    public ResponseEntity<ResponseObject> deleteListProductFromCategory(Integer id, List<Integer> listProductId) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Category not found")
                    .build());
        }
        Category category = categoryRepository.findById(id).get();
        List<BaseProduct> listProduct = category.getProducts();
        for (Integer productId : listProductId) {
            if (productRepository.existsById(productId)) {
                listProduct.remove(productRepository.findById(productId).
                        orElseThrow(() -> new RuntimeException("Product not found")));
            }
        }
        category.setProducts(listProduct);
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Delete list product from category success")
                .data(categoryRepository.save(category))
                .build());
    }

    public ResponseEntity<ResponseObject> getCategoryById(Integer id) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseObject.builder()
                    .responseCode(400)
                    .message("Không tìm thấy danh mục phù hợp")
                    .build());
        }
        Category category = categoryRepository.findById(id).get();
        return ResponseEntity.ok(ResponseObject.builder()
                .responseCode(200)
                .message("Get category by id success")
                .data(category)
                .build());
    }
}
