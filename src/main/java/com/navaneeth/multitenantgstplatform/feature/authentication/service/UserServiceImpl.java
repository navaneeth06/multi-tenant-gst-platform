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

        return userMapper.toResponse(userRepository.save(user));
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

        if (userBranchAccessRepository.existsByUser_IdAndBranch_Id(user.getId(), branchId)) {
            return; // already granted — idempotent, not an error
        }

        userBranchAccessRepository.save(
                UserBranchAccess.builder().user(user).branch(branch).build()
        );
    }

    private Set<Role> resolveRoles(UUID tenantId, List<UUID> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("At least one role must be assigned");
        }

        Set<Role> roles = new HashSet<>();
        for (UUID roleId : roleIds) {
            roles.add(roleRepository.findByIdAndTenantId(roleId, tenantId)
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