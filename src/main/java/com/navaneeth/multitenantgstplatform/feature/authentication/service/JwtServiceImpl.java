package com.navaneeth.multitenantgstplatform.feature.authentication.service;

import com.navaneeth.multitenantgstplatform.common.exception.InvalidTokenException;
import com.navaneeth.multitenantgstplatform.feature.authentication.dto.JwtTokenData;
import com.navaneeth.multitenantgstplatform.feature.authentication.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    @Override
    public String generateAccessToken(User user) {

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expiration);

        // Nullable — a SUPER_ADMIN has no tenant (Module 4).
        String tenantId = user.getTenant() != null
                ? user.getTenant().getId().toString()
                : null;

        List<String> roleIds = user.getRoles().stream()
                .map(role -> role.getId().toString())
                .toList();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("tenantId", tenantId)
                .claim("roleIds", roleIds)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public long getAccessTokenExpiration() {
        return expiration;
    }

    @Override
    public JwtTokenData extractTokenData(String token) {

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            UUID userId = UUID.fromString(claims.getSubject());
            String username = claims.get("username", String.class);

            String tenantIdClaim = claims.get("tenantId", String.class);
            UUID tenantId = tenantIdClaim != null ? UUID.fromString(tenantIdClaim) : null;

            @SuppressWarnings("unchecked")
            List<String> rawRoleIds = claims.get("roleIds", List.class);

            List<UUID> roleIds = rawRoleIds.stream()
                    .map(UUID::fromString)
                    .toList();

            return new JwtTokenData(userId, username, tenantId, roleIds);

        } catch (Exception e) {
            System.out.println("err" + e.getMessage());
            throw new InvalidTokenException("Invalid or expired token");
        }
    }
}