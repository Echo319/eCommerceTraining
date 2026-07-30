package com.rdavies.productcatalog.controller;

import com.rdavies.productcatalog.model.dto.CreateProductRequest;
import com.rdavies.productcatalog.model.dto.ProductResponse;
import com.rdavies.productcatalog.model.dto.UpdateProductRequest;
import com.rdavies.productcatalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request){
        ProductResponse response = service.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@RequestParam(required = false) String category,
                                                                @PageableDefault(size = 20) Pageable pageable ){
        Page<ProductResponse> products = (category != null && !category.isBlank())
                ? service.getProductsByCategory(category, pageable)
                : service.getAllProducts(pageable);

        return ResponseEntity.ok(products);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductResponse> getBySku(@PathVariable String sku){
        return ResponseEntity.ok(service.getProductBySku(sku));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id){
        return ResponseEntity.ok(service.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest request){
        return ResponseEntity.ok(service.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
