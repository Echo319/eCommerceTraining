package com.rdavies.productcataloge.repositories;

import com.rdavies.productcataloge.model.dao.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
