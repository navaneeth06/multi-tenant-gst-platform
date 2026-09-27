package com.navaneeth.multitenantgstplatform.feature.authorization.service;

import com.navaneeth.multitenantgstplatform.feature.authorization.repository.UserBranchAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserBranchAccessService {

    private final UserBranchAccessRepository userBranchAccessRepository;

    public boolean hasAccess(UUID userId, UUID branchId) {
        return userBranchAccessRepository.existsByUser_IdAndBranch_Id(userId, branchId);
    }
}