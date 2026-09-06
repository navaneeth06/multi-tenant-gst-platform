package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.JwtTokenData;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;

import java.util.List;

public interface JwtService {

    String generateAccessToken(
            User user,
            List<JwtTokenData.TenantRoleData> memberships
    );

    long getAccessTokenExpiration();

    JwtTokenData extractTokenData(String token);
}
