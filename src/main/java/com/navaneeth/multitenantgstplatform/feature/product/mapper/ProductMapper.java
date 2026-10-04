package com.navaneeth.multitenantgstplatform.feature.product.mapper;

import com.navaneeth.multitenantgstplatform.feature.product.dto.CreateProductRequest;
import com.navaneeth.multitenantgstplatform.feature.product.dto.ProductResponse;
import com.navaneeth.multitenantgstplatform.feature.product.dto.UpdateProductRequest;
import com.navaneeth.multitenantgstplatform.feature.product.entity.Product;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "hsnCode", ignore = true)
    @Mapping(target = "gstRate", ignore = true)
    @Mapping(target = "unit", ignore = true)
    @Mapping(target = "active", ignore = true)
    Product toEntity(CreateProductRequest request);

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "hsnCode.id", target = "hsnCodeId")
    @Mapping(source = "hsnCode.code", target = "hsnCode")
    @Mapping(source = "gstRate.id", target = "gstRateId")
    @Mapping(source = "gstRate.ratePercent", target = "gstRatePercent")
    @Mapping(source = "unit.id", target = "unitId")
    @Mapping(source = "unit.name", target = "unitName")
    ProductResponse toResponse(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "hsnCode", ignore = true)
    @Mapping(target = "gstRate", ignore = true)
    @Mapping(target = "unit", ignore = true)
    void updateProduct(
            UpdateProductRequest request,
            @MappingTarget Product product
    );
}