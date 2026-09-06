package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.LoginRequest;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);
}
