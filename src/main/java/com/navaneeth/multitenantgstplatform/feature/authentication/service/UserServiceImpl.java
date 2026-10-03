package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.authentication.mapper.UserMapper;
import com.navaneeth.multitenantgstplatform.feature.authentication.repository.UserRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.UserBranchAccess;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.RoleRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.UserBranchAccessRepository;
import com.navaneeth.multitenantgstplatform.feature.branch.entity.Branch;
import com.navaneeth.multitenantgstplatform.feature.branch.repository.BranchRepository;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.feature.tenant.repository.TenantRepository;
import com.navaneeth.multitenantgstplatform.infrastructure.security.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final UserBranchAccessRepository userBranchAccessRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextService securityContextService;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        UUID tenantId = getCurrentTenantId();

        return userRepository.findAllByTenantIdWithDetails(tenantId)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        UUID tenantId = getCurrentTenantId();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Tenant not found"));

        User user = userMapper.toEntity(request);
        user.setTenant(tenant);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRoles(resolveRoles(tenantId, request.getRoleIds()));

        User savedUser = userRepository.save(user);

        if (request.getBranchIds() != null) {
            request.getBranchIds().forEach(branchId ->
                    grantBranchAccessInternal(savedUser, tenantId, branchId));
        }

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {

        UUID tenantId = getCurrentTenantId();

        User user = userRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userMapper.updateUser(request, user);

        if (request.getRoleIds() != null) {
            user.setRoles(resolveRoles(tenantId, request.getRoleIds()));
        }

        if (request.getBranchIds() != null) {
            syncBranchAccess(user, tenantId, request.getBranchIds());
        }

        User updated = userRepository.save(user);

        return userMapper.toResponse(updated);
    }

    /**
     * Replaces the user's branch access with exactly the set provided —
     * grants any new branch not currently held, revokes any currently held
     * branch not in the new list. This mirrors the same "PUT = replace the
     * whole set" semantic used for roles.
     */
    private void syncBranchAccess(User user, UUID tenantId, List<UUID> newBranchIds) {

        Set<UUID> currentBranchIds = userBranchAccessRepository
                .findByUser_Id(user.getId())
                .stream()
                .map(access -> access.getBranch().getId())
                .collect(Collectors.toSet());

        Set<UUID> targetBranchIds = new HashSet<>(newBranchIds);

        // revoke anything no longer in the target set
        currentBranchIds.stream()
                .filter(branchId -> !targetBranchIds.contains(branchId))
                .forEach(branchId -> {
                    userBranchAccessRepository.deleteByUser_IdAndBranch_Id(user.getId(), branchId);
                    user.getBranchAccess().removeIf(access ->
                            access.getBranch().getId().equals(branchId));   // <-- THE FIX for revoke
                });

        // grant anything new
        targetBranchIds.stream()
                .filter(branchId -> !currentBranchIds.contains(branchId))
                .forEach(branchId -> grantBranchAccessInternal(user, tenantId, branchId));
    }

    @Override
    @Transactional
    public UserResponse toggleUserStatus(UUID id) {

        UUID tenantId = getCurrentTenantId();

        User user = userRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setActive(!user.getActive());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void grantBranchAccess(UUID userId, UUID branchId) {

        UUID tenantId = getCurrentTenantId();

        User user = userRepository.findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        grantBranchAccessInternal(user, tenantId, branchId);
    }

    @Override
    @Transactional
    public void revokeBranchAccess(UUID userId, UUID branchId) {

        UUID tenantId = getCurrentTenantId();

        userRepository.findByIdAndTenantId(userId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userBranchAccessRepository.deleteByUser_IdAndBranch_Id(userId, branchId);
    }

    private void grantBranchAccessInternal(User user, UUID tenantId, UUID branchId) {

        Branch branch = branchRepository.findByIdAndTenantId(branchId, tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found for this tenant"));

        boolean alreadyGranted = user.getBranchAccess().stream()
                .anyMatch(access -> access.getBranch().getId().equals(branchId));

        if (alreadyGranted) {
            return; // already granted — idempotent, not an error
        }

        UserBranchAccess access = userBranchAccessRepository.save(
                UserBranchAccess.builder().user(user).branch(branch).build()
        );

        user.getBranchAccess().add(access);   // <-- THE FIX: keep in-memory state in sync
    }

    private Set<Role> resolveRoles(UUID tenantId, List<UUID> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be assigned");
        }

        Set<Role> roles = new HashSet<>();
        for (UUID roleId : roleIds) {
            roles.add(roleRepository.findAssignableByIdAndTenantId(roleId, tenantId)
                    .orElseThrow(() -> new IllegalArgumentException("Role does not belong to this tenant")));
        }
        return roles;
    }

    private UUID getCurrentTenantId() {
        UUID tenantId = securityContextService.getCurrentTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("No authenticated tenant context");
        }
        return tenantId;
    }
}