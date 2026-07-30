package com.rdavies.productcatalog.repositories;

import com.rdavies.productcatalog.model.dao.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
