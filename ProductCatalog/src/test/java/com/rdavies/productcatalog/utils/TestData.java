package com.rdavies.productcatalog.utils;

import com.rdavies.productcatalog.model.dao.Category;
import com.rdavies.productcatalog.model.dao.Product;
import com.rdavies.productcatalog.repositories.CategoryRepository;
import com.rdavies.productcatalog.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@TestComponent
@RequiredArgsConstructor
public class TestData {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    /**
     * Clears all tables in correct relational order.
     */
    @Transactional
    public void clearDatabase() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    /**
     * Seeds standard test categories and products.
     */
    @Transactional
    public void seedDefaultData() {
        clearDatabase();

        // 1. Categories
        Category electronics = categoryRepository.save(Category.builder()
                .name("Electronics")
                .description("Gadgets and hardware")
                .build());

        Category laptops = categoryRepository.save(Category.builder()
                .name("Laptops")
                .description("Portable computers")
                .build());

        Category accessories = categoryRepository.save(Category.builder()
                .name("Accessories")
                .description("Computer peripherals")
                .build());

        // 2. Products
        Product laptop1 = Product.builder()
                .sku("LAPTOP-X1-001")
                .name("ThinkPad X1 Carbon")
                .description("Flagship business ultrabook")
                .price(new BigDecimal("1399.99"))
                .isActive(true)
                .categories(Set.of(electronics, laptops))
                .build();

        Product laptop2 = Product.builder()
                .sku("MBP-16-001")
                .name("MacBook Pro 16")
                .description("M-Series powerhouse")
                .price(new BigDecimal("2499.00"))
                .isActive(true)
                .categories(Set.of(electronics, laptops))
                .build();

        Product mouse = Product.builder()
                .sku("LOGI-MX-001")
                .name("MX Master 3S")
                .description("Ergonomic wireless mouse")
                .price(new BigDecimal("99.99"))
                .isActive(true)
                .categories(Set.of(electronics, accessories))
                .build();

        Product inactiveItem = Product.builder()
                .sku("OLD-GEN-001")
                .name("Legacy Adapter")
                .description("Discontinued item")
                .price(new BigDecimal("14.99"))
                .isActive(false)
                .categories(Set.of(electronics))
                .build();

        productRepository.saveAll(List.of(laptop1, laptop2, mouse, inactiveItem));
    }

    /**
     * Helper to create custom category on demand in tests.
     */
    @Transactional
    public Category createCategory(String name) {
        return categoryRepository.save(Category.builder()
                .name(name)
                .description("Test category " + name)
                .build());
    }

    /**
     * Helper to create a single product on demand.
     */
    @Transactional
    public Product createProduct(String sku, String name, BigDecimal price, Category... categories) {
        return productRepository.save(Product.builder()
                .sku(sku)
                .name(name)
                .price(price)
                .isActive(true)
                .categories(Set.of(categories))
                .build());
    }
}
