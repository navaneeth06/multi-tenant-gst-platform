package com.navaneeth.multitenantgstplatform.authentication.service;

import com.navaneeth.multitenantgstplatform.authentication.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    @Override
    public String generateAccessToken(User user) {

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expiration);

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("tenantId", user.getTenant().getId().toString())
                .claim("username", user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public long getAccessTokenExpiration() {
        return expiration;
    }

}