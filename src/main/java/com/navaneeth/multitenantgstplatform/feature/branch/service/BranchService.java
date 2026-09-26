package com.navaneeth.multitenantgstplatform.feature.branch.service;

import com.navaneeth.multitenantgstplatform.feature.branch.dto.BranchResponse;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.CreateBranchRequest;
import com.navaneeth.multitenantgstplatform.feature.branch.dto.UpdateBranchRequest;

import java.util.List;
import java.util.UUID;

public interface BranchService {

    BranchResponse createBranch(CreateBranchRequest request);

    List<BranchResponse> getBranches();

    BranchResponse getBranch(UUID id);

    BranchResponse updateBranch(
            UUID id,
            UpdateBranchRequest request
    );

    void deactivateBranch(UUID id);
}