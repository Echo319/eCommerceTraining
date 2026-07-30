package com.rdavies.productcatalog.repositories;

import com.rdavies.productcatalog.model.dao.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE c.name = :categoryName AND p.isActive = true")
    Page<Product> findAllByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
}
