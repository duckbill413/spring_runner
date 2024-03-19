package com.example.runner.global.security.service;

import com.example.runner.global.security.dto.TokenDto;
import com.example.runner.global.security.exception.AccessTokenException;
import com.example.runner.global.security.exception.RefreshTokenException;
import com.example.runner.global.security.user.UserSecurityDTO;
import com.example.runner.global.util.JWTUtil;
import com.example.runner.global.util.JwtClaimsParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class JwtService {
    private static final String ACCESS_HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer";

    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    /**
     * 로그인을 요청한 유저의 Request에서 토큰 정보를 가져오고, 권한을 부여한다.
     *
     * @param request 로그인한 유저의 Request
     * @return Access Token에 있던 유저 정보를 기반으로 한 인증 객체
     */
    public Authentication authenticateAccessToken(HttpServletRequest request) {
        String token = requestHeaderJwtParser(request); // access token 정보

        UserSecurityDTO userSecurityDTO;
        if (Objects.isNull(token)) {
            userSecurityDTO = UserSecurityDTO.fromSocial()
                    .username(UUID.randomUUID().toString())
                    .password(UUID.randomUUID().toString())
                    .authorities(JwtClaimsParser.getAnonymousRole())
                    .create();
        } else {
            Claims claims = verifyJwtToken(token);

            userSecurityDTO = UserSecurityDTO.fromSocial()
                    .username(claims.getSubject())
                    .password(UUID.randomUUID().toString())
                    .authorities(JwtClaimsParser.getMemberAuthorities(claims))
                    .create();
        }

        return new UsernamePasswordAuthenticationToken(
                userSecurityDTO,
                userSecurityDTO.getPassword(),
                userSecurityDTO.getAuthorities()
        );
    }

    /**
     * 로그인을 요청한 유저의 Request Header에서 Access Token 정보를 가져온다.
     *
     * @param request 유저의 Request
     * @return Request Header에서 가져온  Access Token 정보
     */
    public String requestHeaderJwtParser(HttpServletRequest request) {
        String token = request.getHeader(ACCESS_HEADER_AUTHORIZATION);
        // access token is null
        if (Objects.isNull(token)) {
//            throw new AccessTokenException(AccessTokenException.ACCESS_TOKEN_ERROR.UN_ACCEPT);
            return null;
        }
        // token type not defined
        String[] separatedToken = token.split(" ");
        if (separatedToken.length != 2) {
            throw new AccessTokenException(AccessTokenException.ACCESS_TOKEN_ERROR.UN_ACCEPT);
        }
        // access token is not bearer type
        if (!separatedToken[0].equalsIgnoreCase(TOKEN_PREFIX)) {
            throw new AccessTokenException(AccessTokenException.ACCESS_TOKEN_ERROR.BAD_TYPE);
        }
        return separatedToken[1];
    }

    // 엑세스 토큰 생성
    public String createAccessToken(UserSecurityDTO userSecurityDTO) {
        return jwtUtil.generateAccessToken(userSecurityDTO);
    }

    public String createRefreshToken(UserSecurityDTO userSecurityDTO) {
        return jwtUtil.generateRefreshToken(userSecurityDTO);
    }

    public TokenDto rotateJwtTokens(String refreshToken) throws RefreshTokenException {
        Claims claims = verifyJwtToken(refreshToken);
        String id = claims.getSubject();

        boolean isMatching = jwtUtil.isMatching(UUID.fromString(id), refreshToken);
        // 서버의 refresh token 과 일치하지 않는 경우
        if (!isMatching) {
            throw new RefreshTokenException(RefreshTokenException.REFRESH_TOKEN_ERROR.BAD_REFRESH);
        }

        UserSecurityDTO userSecurityDTO = userDetailsService.loadUserByUsername(id);
        String newAccessToken = createAccessToken(userSecurityDTO);
        String newRefreshToken = createRefreshToken(userSecurityDTO);
        return new TokenDto(
                newAccessToken,
                newRefreshToken,
                userSecurityDTO.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }

    public Claims verifyJwtToken(String token) {
        try {
            return jwtUtil.verifyJwtToken(token);
        } catch (MalformedJwtException malformedJwtException) {
            throw new AccessTokenException(AccessTokenException.ACCESS_TOKEN_ERROR.MAL_FORM);
        } catch (SignatureException signatureException) {
            throw new AccessTokenException(AccessTokenException.ACCESS_TOKEN_ERROR.BAD_SIGN);
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new AccessTokenException(AccessTokenException.ACCESS_TOKEN_ERROR.BAD_TYPE);
        } catch (ExpiredJwtException expiredJwtException) {
            throw new AccessTokenException(AccessTokenException.ACCESS_TOKEN_ERROR.EXPIRED);
        }
    }
}