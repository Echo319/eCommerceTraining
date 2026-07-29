package com.rdavies.productcataloge.service;

import com.rdavies.productcataloge.model.dto.CreateProductRequest;
import com.rdavies.productcataloge.model.dto.ProductResponse;
import com.rdavies.productcataloge.model.dto.UpdateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductResponse createProduct(CreateProductRequest request);

    ProductResponse getProductBySku(String sku);

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> getProductsByCategory(String category, Pageable pageable);

    ProductResponse updateProduct(Long id, UpdateProductRequest request);

    void deleteProduct(Long id);
}
