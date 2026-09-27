package com.navaneeth.multitenantgstplatform.feature.master.mapper;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateGSTRateRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.GSTRateResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateGSTRateRequest;
import com.navaneeth.multitenantgstplatform.feature.master.entity.GSTRate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface GSTRateMapper {

    @Mapping(target = "active", ignore = true)
    GSTRate toEntity(CreateGSTRateRequest request);

    GSTRateResponse toResponse(GSTRate gstRate);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateGSTRate(
            UpdateGSTRateRequest request,
            @MappingTarget GSTRate gstRate
    );
}