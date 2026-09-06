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
import java.util.Map;
import java.util.UUID;

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
    public String generateAccessToken(
            User user,
            List<JwtTokenData.TenantRoleData> memberships
    ) {

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expiration);

        List<Map<String, String>> membershipClaims =
                memberships.stream()
                        .map(membership -> Map.of(
                                "tenantId",
                                membership.tenantId().toString(),
                                "roleId",
                                membership.roleId().toString()
                        ))
                        .toList();

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("username", user.getUsername())
                .claim("memberships", membershipClaims)
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

            UUID userId =
                    UUID.fromString(claims.getSubject());

            String username =
                    claims.get("username", String.class);

            List<Map<String, String>> rawMemberships =
                    claims.get("memberships", List.class);

            List<JwtTokenData.TenantRoleData> memberships =
                    rawMemberships.stream()
                            .map(membership ->
                                    new JwtTokenData.TenantRoleData(
                                            UUID.fromString(
                                                    membership.get("tenantId")
                                            ),
                                            UUID.fromString(
                                                    membership.get("roleId")
                                            )
                                    )
                            )
                            .toList();

            return new JwtTokenData(
                    userId,
                    username,
                    memberships
            );

        } catch (Exception e) {

            throw new InvalidTokenException(
                    "Invalid or expired token"
            );
        }
    }
}