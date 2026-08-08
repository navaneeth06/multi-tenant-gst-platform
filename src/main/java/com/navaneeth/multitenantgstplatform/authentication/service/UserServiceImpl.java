package com.navaneeth.multitenantgstplatform.authentication.service;

import com.navaneeth.multitenantgstplatform.authentication.dto.CreateUserRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.UpdateUserRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.UserResponse;
import com.navaneeth.multitenantgstplatform.authentication.entity.User;
import com.navaneeth.multitenantgstplatform.authentication.mapper.UserMapper;
import com.navaneeth.multitenantgstplatform.authentication.repository.UserRepository;
import com.navaneeth.multitenantgstplatform.tenant.entity.Tenant;
import com.navaneeth.multitenantgstplatform.tenant.repository.TenantRepository;
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
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            TenantRepository tenantRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        User user = userMapper.toEntity(request);

        user.setTenant(tenant);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    @Transactional
    @Override
    public UserResponse updateUser(
            UUID id,
            UpdateUserRequest request
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(request, user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse toggleUserStatus(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(!user.getActive());

        User updatedUser = userRepository.save(user);

        return userMapper.toResponse(updatedUser);
    }
}