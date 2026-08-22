package com.navaneeth.multitenantgstplatform.authentication.service;

import com.navaneeth.multitenantgstplatform.authentication.dto.LoginRequest;
import com.navaneeth.multitenantgstplatform.authentication.dto.LoginResponse;

import java.util.UUID;

public interface AuthService {

    LoginResponse login(UUID tenantId, LoginRequest request);
}
