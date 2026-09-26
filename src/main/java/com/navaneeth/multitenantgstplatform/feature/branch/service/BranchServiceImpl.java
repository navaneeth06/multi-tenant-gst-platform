package com.navaneeth.multitenantgstplatform.feature.branch.service;

import com.navaneeth.multitenantgstplatform.feature.branch.dto.BranchResponse;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.CreateBranchRequest;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.UpdateBranchRequest;
import com.navaneeth.multitenantgstplatform.feature.branch.entity.Branch;
import com.navaneeth.multitenantgstplatform.feature.branch.mapper.BranchMapper;
import com.navaneeth.multitenantgstplatform.feature.branch.repository.BranchRepository;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.entity.GSTRegistration;
import com.navaneeth.multitenantgstplatform.feature.gstregistration.repository.GSTRegistrationRepository;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.feature.tenant.repository.TenantRepository;
import com.navaneeth.multitenantgstplatform.infrastructure.security.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;
    private final TenantRepository tenantRepository;
    private final GSTRegistrationRepository gstRegistrationRepository;
    private final BranchMapper branchMapper;
    private final SecurityContextService securityContextService;

    @Override
    @Transactional
    public BranchResponse createBranch(CreateBranchRequest request) {

        UUID tenantId = getCurrentTenantId();

        if (branchRepository.existsByTenantIdAndName(
                tenantId,
                request.name()
        )) {
            throw new IllegalArgumentException(
                    "Branch with this name already exists"
            );
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Tenant not found"
                        )
                );

        // Loading via findByIdAndTenantId (not findById) is deliberate here —
        // it guarantees the registration actually belongs to this tenant,
        // not just that some registration with this ID exists somewhere.
        GSTRegistration gstRegistration = gstRegistrationRepository
                .findByIdAndTenantId(request.gstRegistrationId(), tenantId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "GST registration not found for this tenant"
                        )
                );

        Branch branch = branchMapper.toEntity(request);

        branch.setTenant(tenant);
        branch.setGstRegistration(gstRegistration);


        branch.setStatus(Branch.BranchStatus.ACTIVE);

        Branch savedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(savedBranch);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BranchResponse> getBranches() {

        UUID tenantId = getCurrentTenantId();

        return branchRepository.findByTenantId(tenantId)
                .stream()
                .map(branchMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public BranchResponse getBranch(UUID id) {

        UUID tenantId = getCurrentTenantId();

        Branch branch = branchRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Branch not found"
                        )
                );

        return branchMapper.toResponse(branch);
    }


    @Override
    @Transactional
    public BranchResponse updateBranch(
            UUID id,
            UpdateBranchRequest request
    ) {

        UUID tenantId = getCurrentTenantId();

        Branch branch = branchRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Branch not found"
                        )
                );

        if (request.name() != null &&
                !request.name().equals(branch.getName())) {

            if (branchRepository.existsByTenantIdAndName(
                    tenantId,
                    request.name()
            )) {
                throw new IllegalArgumentException(
                        "Branch with this name already exists"
                );
            }
        }

        branchMapper.updateBranch(request, branch);

        // Deliberately no gstRegistrationId/stateId handling here.
        // A branch's registration (and therefore its state) is fixed at
        // creation time and is not editable — same reasoning as
        // GSTRegistration itself having no update endpoint. Moving a branch
        // to a different registration is a "close this branch, open a new
        // one" operation, not a field edit.

        Branch updatedBranch = branchRepository.save(branch);

        return branchMapper.toResponse(updatedBranch);
    }


    @Override
    @Transactional
    public void deactivateBranch(UUID id) {

        UUID tenantId = getCurrentTenantId();

        Branch branch = branchRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Branch not found"
                        )
                );

        branch.setStatus(Branch.BranchStatus.INACTIVE);

        branchRepository.save(branch);
    }


    private UUID getCurrentTenantId() {

        UUID tenantId = securityContextService.getCurrentTenantId();

        if (tenantId == null) {
            throw new IllegalStateException(
                    "No authenticated tenant context"
            );
        }

        return tenantId;
    }
}