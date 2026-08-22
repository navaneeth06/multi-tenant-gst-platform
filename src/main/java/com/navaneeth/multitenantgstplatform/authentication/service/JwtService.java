package com.navaneeth.multitenantgstplatform.authentication.service;

import com.navaneeth.multitenantgstplatform.authentication.entity.User;

public interface JwtService {

    String generateAccessToken(User user);

    long getAccessTokenExpiration();
}
