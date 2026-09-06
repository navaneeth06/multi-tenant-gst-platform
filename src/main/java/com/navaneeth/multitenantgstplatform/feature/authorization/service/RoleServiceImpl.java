package com.navaneeth.multitenantgstplatform.feature.authorization.service;

import com.navaneeth.multitenantgstplatform.common.exception.AppException;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.CreateRoleRequest;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RoleResponse;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.UpdateRoleRequest;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import com.navaneeth.multitenantgstplatform.feature.authorization.mapper.RoleMapper;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.RoleRepository;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.feature.tenant.repository.TenantRepository;
import com.navaneeth.multitenantgstplatform.infrastructure.security.SecurityContextService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final SecurityContextService securityContextService;
    private final TenantRepository tenantRepository;

    public RoleServiceImpl(
            RoleRepository roleRepository,
            RoleMapper roleMapper,
            SecurityContextService securityContextService,
            TenantRepository tenantRepository
    ) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.securityContextService = securityContextService;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    @Override
    public RoleResponse createRole(CreateRoleRequest request) {

        UUID tenantId =
                securityContextService.getCurrentTenantId();
        if (tenantId == null) {
            tenantId = request.tenantId();
        }
        if (roleRepository.existsByTenantIdAndSlug(
                tenantId,
                request.slug()
        )) {
            throw new AppException(
                    "Role slug already exists",
                    HttpStatus.CONFLICT
            );
        }

        Tenant tenant = tenantRepository
                .findById(tenantId)
                .orElseThrow(() ->
                        new AppException(
                                "Tenant not found",
                                HttpStatus.NOT_FOUND
                        )
                );

        Role role = roleMapper.toEntity(request);

        role.setTenant(tenant);

        Role savedRole = roleRepository.save(role);

        return roleMapper.toResponse(savedRole);
    }

    @Override
    public List<RoleResponse> getRoles() {

        UUID tenantId =
                securityContextService.getCurrentTenantId();

        return roleRepository
                .findByTenantId(tenantId)
                .stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public RoleResponse updateRole(
            UUID id,
            UpdateRoleRequest request
    ) {

        UUID tenantId =
                securityContextService.getCurrentTenantId();

        Role role = roleRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new AppException(
                                "Role not found",
                                HttpStatus.NOT_FOUND
                        )
                );

        roleMapper.updateRole(request, role);

        Role savedRole = roleRepository.save(role);

        return roleMapper.toResponse(savedRole);
    }
}