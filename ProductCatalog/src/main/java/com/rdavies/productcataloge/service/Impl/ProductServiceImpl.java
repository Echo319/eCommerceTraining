package com.rdavies.productcataloge.service.Impl;

import com.rdavies.productcataloge.model.dao.CategoryDao;
import com.rdavies.productcataloge.model.dao.ProductDao;
import com.rdavies.productcataloge.model.dto.CreateProductRequest;
import com.rdavies.productcataloge.model.dto.ProductResponse;
import com.rdavies.productcataloge.model.dto.UpdateProductRequest;
import com.rdavies.productcataloge.model.mapper.ProductMapper;
import com.rdavies.productcataloge.repositories.CategoryRepository;
import com.rdavies.productcataloge.repositories.ProductRepository;
import com.rdavies.productcataloge.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper mapper;


    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if(!productRepository.existsBySku(request.sku())) {
            throw new RuntimeException("Product already exists");
        }

        ProductDao product = mapper.toEntity(request);
        Set<CategoryDao> categories = new HashSet<>(categoryRepository.findAllById(request.categoryIds()));
        product.setCategories(categories);

        ProductDao created = productRepository.save(product);
        return mapper.toDto(created);
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        ProductDao product = productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException(String.format("Could not find resource %s", sku)));
        return mapper.toDto(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        ProductDao product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Could not find resource %d", id)));
        return mapper.toDto(product);
    }

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findAllByCategoryName(category, pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        ProductDao product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("No such product with Id %d", id)));

        mapper.updateEntityFromDto(request, product);

        if(request.categoryIds() != null && !request.categoryIds().isEmpty()) {
            Set<CategoryDao> categories = new HashSet<>(categoryRepository.findAllById(request.categoryIds()));
            product.setCategories(categories);
        }

        ProductDao updated = productRepository.save(product);
        return mapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new RuntimeException(String.format("No product with id of %d", id));
        }
        productRepository.deleteById(id);
    }
}
