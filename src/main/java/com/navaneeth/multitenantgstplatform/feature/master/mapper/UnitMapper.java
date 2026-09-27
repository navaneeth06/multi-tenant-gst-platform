package com.navaneeth.multitenantgstplatform.feature.master.mapper;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateUnitRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UnitResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateUnitRequest;
import com.navaneeth.multitenantgstplatform.feature.master.entity.Unit;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    Unit toEntity(CreateUnitRequest request);

    UnitResponse toResponse(Unit unit);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUnit(
            UpdateUnitRequest request,
            @MappingTarget Unit unit
    );
}