package com.navaneeth.multitenantgstplatform.feature.authorization.repository;

import com.navaneeth.multitenantgstplatform.feature.authorization.entity.UserBranchAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserBranchAccessRepository
        extends JpaRepository<UserBranchAccess, UserBranchAccess.UserBranchAccessId> {

    boolean existsByUser_IdAndBranch_Id(UUID userId, UUID branchId);

    List<UserBranchAccess> findByUser_Id(UUID userId);

    void deleteByUser_IdAndBranch_Id(UUID userId, UUID branchId);
}