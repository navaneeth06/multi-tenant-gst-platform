package com.navaneeth.multitenantgstplatform.feature.branch.mapper;

import com.navaneeth.multitenantgstplatform.feature.branch.dto.BranchResponse;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.CreateBranchRequest;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.UpdateBranchRequest;
import com.navaneeth.multitenantgstplatform.feature.branch.entity.Branch;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "status", ignore = true)
    Branch toEntity(CreateBranchRequest request);

    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "state.id", target = "stateId")
    @Mapping(source = "state.name", target = "stateName")
    BranchResponse toResponse(Branch branch);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateBranch(
            UpdateBranchRequest request,
            @MappingTarget Branch branch
    );
}