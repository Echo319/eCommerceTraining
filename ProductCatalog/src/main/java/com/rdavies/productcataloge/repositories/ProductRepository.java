package com.rdavies.productcataloge.repositories;

import com.rdavies.productcataloge.model.dao.ProductDao;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductDao, Long> {

    Optional<ProductDao> findBySku(String sku);

    boolean existsBySku(String sku);

    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.categories WHERE p.sku = :sku AND p.isActive = true")
    Optional<ProductDao> findBySkuWithCategory(@Param("sku") String sku);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE c.name = :categoryName AND p.isActive = true")
    Page<ProductDao> findAllByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);
}
