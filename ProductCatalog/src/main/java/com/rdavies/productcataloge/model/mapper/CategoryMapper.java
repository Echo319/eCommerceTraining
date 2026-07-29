package com.rdavies.productcataloge.model.mapper;

import com.rdavies.productcataloge.model.dao.CategoryDao;
import com.rdavies.productcataloge.model.dto.CategoryResponse;
import com.rdavies.productcataloge.model.dto.CreateCategoryRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "string")
public interface CategoryMapper {

    CategoryResponse toDto(CategoryDao categoryDao);

    @Mapping(target = "id", ignore = true)
    CategoryDao toEntity(CreateCategoryRequest request);
}
