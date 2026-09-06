package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.feature.authentication.mapper.UserMapper;
import com.navaneeth.multitenantgstplatform.feature.authentication.repository.UserRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.Role;
import com.navaneeth.multitenantgstplatform.feature.authorization.entity.TenantUser;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.RoleRepository;
import com.navaneeth.multitenantgstplatform.feature.authorization.repository.TenantUserRepository;
import com.navaneeth.multitenantgstplatform.feature.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.feature.tenant.repository.TenantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TenantRepository tenantRepository;
    private final TenantUserRepository tenantUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            TenantRepository tenantRepository,
            TenantUserRepository tenantUserRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tenantRepository = tenantRepository;
        this.tenantUserRepository = tenantUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {

        return userRepository.findAllWithMemberships()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    public UserResponse createUser(CreateUserRequest request) {

        User user = userMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setActive(true);

        User savedUser = userRepository.save(user);

        for (CreateUserRequest.TenantRoleRequest membership
                : request.getMemberships()) {

            Tenant tenant = tenantRepository
                    .findById(membership.getTenantId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Tenant not found: "
                                            + membership.getTenantId()
                            )
                    );

            Role role = roleRepository
                    .findByIdAndTenantId(
                            membership.getRoleId(),
                            membership.getTenantId()
                    )
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Role does not belong to the selected tenant"
                            )
                    );

            TenantUser tenantUser = TenantUser.builder()
                    .tenant(tenant)
                    .user(savedUser)
                    .role(role)
                    .build();

            tenantUserRepository.save(tenantUser);
        }

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    @Override
    public UserResponse updateUser(
            UUID id,
            UpdateUserRequest request
    ) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        userMapper.updateUser(request, user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse toggleUserStatus(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        user.setActive(!user.getActive());

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }
}