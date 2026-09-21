package com.rdavies.productcatalog.service;

import com.rdavies.productcatalog.exceptions.DuplicateResourceException;
import com.rdavies.productcatalog.exceptions.ResourceNotFoundException;
import com.rdavies.productcatalog.model.dao.Category;
import com.rdavies.productcatalog.model.dao.Product;
import com.rdavies.productcatalog.model.dto.CategoryResponse;
import com.rdavies.productcatalog.model.dto.CreateProductRequest;
import com.rdavies.productcatalog.model.dto.ProductResponse;
import com.rdavies.productcatalog.model.dto.UpdateProductRequest;
import com.rdavies.productcatalog.model.mapper.ProductMapper;
import com.rdavies.productcatalog.repositories.CategoryRepository;
import com.rdavies.productcatalog.repositories.ProductRepository;
import com.rdavies.productcatalog.service.Impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper mapper;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void createProductSavesProductWithResolvedCategories() {
        Category electronics = category(1L, "Electronics");
        Category laptops = category(2L, "Laptops");
        Product mappedProduct = product(null, "LAPTOP-001", "Laptop");
        Product savedProduct = product(10L, "LAPTOP-001", "Laptop");
        ProductResponse response = productResponse(10L, "LAPTOP-001", "Laptop", Set.of(
                new CategoryResponse(1L, "Electronics", "Category Electronics"),
                new CategoryResponse(2L, "Laptops", "Category Laptops")
        ));
        CreateProductRequest request = new CreateProductRequest(
                "LAPTOP-001",
                "Laptop",
                "Portable workstation",
                new BigDecimal("1299.99"),
                Set.of(1L, 2L)
        );

        when(productRepository.existsBySku("LAPTOP-001")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(mappedProduct);
        when(categoryRepository.findAllById(Set.of(1L, 2L))).thenReturn(List.of(electronics, laptops));
        when(productRepository.save(mappedProduct)).thenReturn(savedProduct);
        when(mapper.toDto(savedProduct)).thenReturn(response);

        ProductResponse result = service.createProduct(request);

        assertThat(result).isEqualTo(response);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        assertThat(productCaptor.getValue().getCategories())
                .extracting(Category::getName)
                .containsExactlyInAnyOrder("Electronics", "Laptops");
    }

    @Test
    void createProductRejectsDuplicateSkuBeforeMapping() {
        CreateProductRequest request = new CreateProductRequest(
                "LAPTOP-001",
                "Laptop",
                "Portable workstation",
                new BigDecimal("1299.99"),
                Set.of(1L)
        );
        when(productRepository.existsBySku("LAPTOP-001")).thenReturn(true);

        assertThatThrownBy(() -> service.createProduct(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Product already exists");

        verify(mapper, never()).toEntity(any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProductRejectsMissingCategories() {
        CreateProductRequest request = new CreateProductRequest(
                "LAPTOP-001",
                "Laptop",
                "Portable workstation",
                new BigDecimal("1299.99"),
                Set.of(1L, 2L)
        );
        Product mappedProduct = product(null, "LAPTOP-001", "Laptop");
        when(productRepository.existsBySku("LAPTOP-001")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(mappedProduct);
        when(categoryRepository.findAllById(Set.of(1L, 2L))).thenReturn(List.of(category(1L, "Electronics")));

        assertThatThrownBy(() -> service.createProduct(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Categories not found")
                .hasMessageContaining("2");

        verify(productRepository, never()).save(any());
    }

    @Test
    void getProductBySkuReturnsMappedProduct() {
        Product product = product(10L, "LAPTOP-001", "Laptop");
        ProductResponse response = productResponse(10L, "LAPTOP-001", "Laptop", Set.of());
        when(productRepository.findBySku("LAPTOP-001")).thenReturn(Optional.of(product));
        when(mapper.toDto(product)).thenReturn(response);

        assertThat(service.getProductBySku("LAPTOP-001")).isEqualTo(response);
    }

    @Test
    void getProductByIdThrowsWhenProductDoesNotExist() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProductById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Could not find resource 99");
    }

    @Test
    void getProductsByCategoryDelegatesToCategoryQuery() {
        PageRequest pageable = PageRequest.of(0, 5);
        Product product = product(10L, "LAPTOP-001", "Laptop");
        ProductResponse response = productResponse(10L, "LAPTOP-001", "Laptop", Set.of());
        when(productRepository.findAllByCategoryName("Laptops", pageable)).thenReturn(new PageImpl<>(List.of(product)));
        when(mapper.toDto(product)).thenReturn(response);

        assertThat(service.getProductsByCategory("Laptops", pageable).getContent())
                .containsExactly(response);
    }

    @Test
    void updateProductPreservesCategoriesWhenCategoryIdsAreNull() {
        Product product = product(10L, "LAPTOP-001", "Laptop");
        Category electronics = category(1L, "Electronics");
        product.setCategories(Set.of(electronics));
        UpdateProductRequest request = new UpdateProductRequest(
                "Updated Laptop",
                null,
                new BigDecimal("1199.99"),
                null,
                null
        );
        ProductResponse response = productResponse(10L, "LAPTOP-001", "Updated Laptop", Set.of(
                new CategoryResponse(1L, "Electronics", "Category Electronics")
        ));

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(response);

        ProductResponse result = service.updateProduct(10L, request);

        assertThat(result).isEqualTo(response);
        assertThat(product.getCategories()).containsExactly(electronics);
        verify(categoryRepository, never()).findAllById(any());
        verify(mapper).updateEntityFromDto(request, product);
    }

    @Test
    void updateProductReplacesCategoriesWhenCategoryIdsAreProvided() {
        Product product = product(10L, "LAPTOP-001", "Laptop");
        Category laptops = category(2L, "Laptops");
        UpdateProductRequest request = new UpdateProductRequest(null, null, null, true, Set.of(2L));
        ProductResponse response = productResponse(10L, "LAPTOP-001", "Laptop", Set.of(
                new CategoryResponse(2L, "Laptops", "Category Laptops")
        ));

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        when(categoryRepository.findAllById(Set.of(2L))).thenReturn(List.of(laptops));
        when(productRepository.save(product)).thenReturn(product);
        when(mapper.toDto(product)).thenReturn(response);

        ProductResponse result = service.updateProduct(10L, request);

        assertThat(result).isEqualTo(response);
        assertThat(product.getCategories()).containsExactly(laptops);
    }

    @Test
    void deleteProductDeletesExistingProduct() {
        when(productRepository.existsById(10L)).thenReturn(true);

        service.deleteProduct(10L);

        verify(productRepository).deleteById(10L);
    }

    @Test
    void deleteProductThrowsWhenProductDoesNotExist() {
        when(productRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> service.deleteProduct(10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No product with id of 10");

        verify(productRepository, never()).deleteById(any());
    }

    private static Product product(Long id, String sku, String name) {
        return Product.builder()
                .id(id)
                .sku(sku)
                .name(name)
                .description("Product " + name)
                .price(new BigDecimal("99.99"))
                .isActive(true)
                .build();
    }

    private static Category category(Long id, String name) {
        return Category.builder()
                .id(id)
                .name(name)
                .description("Category " + name)
                .build();
    }

    private static ProductResponse productResponse(Long id, String sku, String name, Set<CategoryResponse> categories) {
        return new ProductResponse(
                id,
                sku,
                name,
                "Product " + name,
                new BigDecimal("99.99"),
                true,
                categories,
                null,
                null
        );
    }
}
