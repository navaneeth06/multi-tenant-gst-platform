package com.navaneeth.multitenantgstplatform.authorization.service;

import com.navaneeth.multitenantgstplatform.authorization.dto.CreateRoleRequest;
import com.navaneeth.multitenantgstplatform.authorization.dto.RoleResponse;
import com.navaneeth.multitenantgstplatform.authorization.dto.UpdateRoleRequest;
import com.navaneeth.multitenantgstplatform.authorization.entity.Role;
import com.navaneeth.multitenantgstplatform.authorization.mapper.RoleMapper;
import com.navaneeth.multitenantgstplatform.authorization.repository.RoleRepository;
import com.navaneeth.multitenantgstplatform.common.exception.AppException;
import com.navaneeth.multitenantgstplatform.common.security.SecurityContextService;
import com.navaneeth.multitenantgstplatform.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.tenant.repository.TenantRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final SecurityContextService securityContextService;
    private final TenantRepository tenantRepository;

    public RoleServiceImpl(
            RoleMapper roleMapper,
            RoleRepository roleRepository,
            SecurityContextService securityContextService,
            TenantRepository tenantRepository

    ) {
        this.roleMapper = roleMapper;
        this.roleRepository = roleRepository;
        this.securityContextService = securityContextService;
        this.tenantRepository = tenantRepository;
    }

    public RoleResponse createRole(CreateRoleRequest request) {

        UUID tenantId = securityContextService.getCurrentTenantId();

        if (roleRepository.existsByTenantIdAndSlug(
                tenantId,
                request.slug()
        )) {
            throw new AppException("Role slug already exists", HttpStatus.CONFLICT);
        }


        Tenant tenant = this.tenantRepository
                .findById(tenantId)
                .orElseThrow(() ->
                        new AppException("Tenant not found", HttpStatus.NOT_FOUND)
                );

        Role role = roleMapper.toEntity(request);

        role.setTenant(tenant);

        Role savedRole = roleRepository.save(role);

        return roleMapper.toResponse(savedRole);
    }

    public List<RoleResponse> getRoles() {
        UUID tenantId = this.securityContextService.getCurrentTenantId();
        List<Role> roles = roleRepository.findByTenantId(tenantId);
        return roles.stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    public RoleResponse updateRole(UUID id, UpdateRoleRequest request) {

        UUID tenantId = securityContextService.getCurrentTenantId();

        Role role = roleRepository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new AppException("Role not found", HttpStatus.NOT_FOUND)
                );

        if (roleRepository.existsByTenantIdAndSlug(tenantId, request.name())
                && !role.getSlug().equals(request.name())) {

            throw new AppException("Role name already exists", HttpStatus.CONFLICT);
        }

        roleMapper.updateRole(request, role);

        Role savedRole = roleRepository.save(role);

        return roleMapper.toResponse(savedRole);
    }

}
