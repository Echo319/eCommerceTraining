package com.rdavies.productcatalog.model.mapper;

import com.rdavies.productcatalog.model.dao.Product;
import com.rdavies.productcatalog.model.dto.CreateProductRequest;
import com.rdavies.productcatalog.model.dto.ProductResponse;
import com.rdavies.productcatalog.model.dto.UpdateProductRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(CreateProductRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateProductRequest request, @MappingTarget Product entity);


}
