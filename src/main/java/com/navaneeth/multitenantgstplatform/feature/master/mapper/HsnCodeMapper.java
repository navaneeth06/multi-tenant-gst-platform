package com.navaneeth.multitenantgstplatform.feature.master.mapper;

import com.navaneeth.multitenantgstplatform.feature.master.dto.CreateHsnCodeRequest;
import com.navaneeth.multitenantgstplatform.feature.master.dto.HsnCodeResponse;
import com.navaneeth.multitenantgstplatform.feature.master.dto.UpdateHsnCodeRequest;
import com.navaneeth.multitenantgstplatform.feature.master.entity.HsnCode;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface HsnCodeMapper {

    @Mapping(target = "gstRate", ignore = true)
    HsnCode toEntity(CreateHsnCodeRequest request);

    @Mapping(source = "gstRate.id", target = "gstRateId")
    @Mapping(source = "gstRate.ratePercent", target = "gstRatePercent")
    HsnCodeResponse toResponse(HsnCode hsnCode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "gstRate", ignore = true)
    void updateHsnCode(
            UpdateHsnCodeRequest request,
            @MappingTarget HsnCode hsnCode
    );
}