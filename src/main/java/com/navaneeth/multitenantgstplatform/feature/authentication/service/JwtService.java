package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.feature.authentication.dto.JwtTokenData;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;

public interface JwtService {

    String generateAccessToken(User user);

    long getAccessTokenExpiration();

    JwtTokenData extractTokenData(String token);
}