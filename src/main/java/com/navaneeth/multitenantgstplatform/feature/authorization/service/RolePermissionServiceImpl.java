package com.navaneeth.multitenantgstplatform.feature.authorization.service;

import com.navaneeth.multitenantgstplatform.common.exception.AppException;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RolePermissionRequest;
import com.navaneeth.multitenantgstplatform.feature.authorization.dto.RolePermissionResponse;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Permission;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.PermissionRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional(readOnly = true)
    public RolePermissionResponse getPermissions(UUID roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new AppException(
                                "Role not found: " + roleId,
                                HttpStatus.NOT_FOUND
                        )
                );

        Set<RolePermissionResponse.PermissionItem> permissions =
                role.getPermissions()
                        .stream()
                        .map(permission ->
                                new RolePermissionResponse.PermissionItem(
                                        permission.getId(),
                                        permission.getModule().getId(),
                                        permission.getModule().getName(),
                                        permission.getAction()
                                )
                        )
                        .collect(Collectors.toSet());

        return new RolePermissionResponse(
                role.getId(),
                role.getName(),
                permissions
        );
    }

    @Override
    public RolePermissionResponse updatePermissions(
            UUID roleId,
            RolePermissionRequest request
    ) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new AppException(
                                "Role not found: " + roleId,
                                HttpStatus.NOT_FOUND
                        )
                );

        Set<Permission> permissions =
                new HashSet<>(
                        permissionRepository.findAllById(
                                request.permissionIds()
                        )
                );

        if (permissions.size() != request.permissionIds().size()) {
            throw new AppException(
                    "One or more permissions not found",
                    HttpStatus.NOT_FOUND
            );
        }

        role.setPermissions(permissions);

        Role savedRole = roleRepository.save(role);

        Set<RolePermissionResponse.PermissionItem> responsePermissions =
                savedRole.getPermissions()
                        .stream()
                        .map(permission ->
                                new RolePermissionResponse.PermissionItem(
                                        permission.getId(),
                                        permission.getModule().getId(),
                                        permission.getModule().getName(),
                                        permission.getAction()
                                )
                        )
                        .collect(Collectors.toSet());

        return new RolePermissionResponse(
                savedRole.getId(),
                savedRole.getName(),
                responsePermissions
        );
    }
}