package com.rdavies.productcataloge.service.Impl;

import com.rdavies.productcataloge.exceptions.DuplicateResourceException;
import com.rdavies.productcataloge.exceptions.ResouceNotFoundException;
import com.rdavies.productcataloge.model.dao.Category;
import com.rdavies.productcataloge.model.dao.Product;
import com.rdavies.productcataloge.model.dto.CreateProductRequest;
import com.rdavies.productcataloge.model.dto.ProductResponse;
import com.rdavies.productcataloge.model.dto.UpdateProductRequest;
import com.rdavies.productcataloge.model.mapper.ProductMapper;
import com.rdavies.productcataloge.repositories.CategoryRepository;
import com.rdavies.productcataloge.repositories.ProductRepository;
import com.rdavies.productcataloge.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper mapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }


    @Override
    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        if(productRepository.existsBySku(request.sku())) {
            throw new DuplicateResourceException("Product already exists");
        }

        Product product = mapper.toEntity(request);
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.categoryIds()));
        product.setCategories(categories);

        Product created = productRepository.save(product);
        return mapper.toDto(created);
    }

    @Override
    public ProductResponse getProductBySku(String sku) {
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResouceNotFoundException(String.format("Could not find resource %s", sku)));
        return mapper.toDto(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException(String.format("Could not find resource %d", id)));
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
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResouceNotFoundException(String.format("No such product with Id %d", id)));

        mapper.updateEntityFromDto(request, product);

        if(request.categoryIds() != null && !request.categoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.categoryIds()));
            product.setCategories(categories);
        }

        Product updated = productRepository.save(product);
        return mapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new ResouceNotFoundException(String.format("No product with id of %d", id));
        }
        productRepository.deleteById(id);
    }
}
