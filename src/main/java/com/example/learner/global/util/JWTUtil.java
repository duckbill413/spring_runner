package com.example.learner.global.util;

import com.example.learner.global.security.TokenRedisDao;
import com.example.learner.global.security.user.UserSecurityDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * author        : duckbill413
 * date          : 2023-04-26
 * description   :
 **/
@Log4j2
@Component
public class JWTUtil {
    private final SecretKey secretKey;
    private final long accessTokenExpireTime;
    private final long refreshTokenExpireTime;
    private final String issuer;
    private static final String ACCESS_HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final TokenRedisDao tokenRedisDao;

    public JWTUtil(
            @Value(value = "${jwt.key.salt}")
            String jwtKey,
            @Value(value = "${jwt.expire-time.access-token}")
            long accessTokenExpireTime,
            @Value(value = "${jwt.expire-time.refresh-token}")
            long refreshTokenExpireTime,
            @Value(value = "${jwt.issuer}")
            String issuer, TokenRedisDao tokenRedisDao
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.issuer = issuer;
        this.tokenRedisDao = tokenRedisDao;
    }

    public String generateAccessToken(UserSecurityDTO userSecurityDTO) {
        return generateToken(userSecurityDTO, accessTokenExpireTime);
    }

    public String generateRefreshToken(UserSecurityDTO userSecurityDTO) {
        var refreshToken = generateToken(userSecurityDTO, refreshTokenExpireTime);

        // refresh token 발급시 redis에 저장
        tokenRedisDao.save(userSecurityDTO.getId(), refreshToken, refreshTokenExpireTime);
        return refreshToken;
    }

    private String generateToken(UserSecurityDTO userSecurityDTO, long refreshTokenExpireTime) {
        Date expirationDate = new Date(new Date().getTime() + refreshTokenExpireTime * 1000);

        return Jwts.builder()
                .signWith(secretKey, Jwts.SIG.HS256)
                .issuer(issuer)
                .expiration(expirationDate)
                .subject(userSecurityDTO.getUsername())
                .claim("roles", userSecurityDTO.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .compact();
    }

    public Claims verifyJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
