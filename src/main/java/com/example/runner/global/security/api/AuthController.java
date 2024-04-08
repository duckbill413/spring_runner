package com.example.runner.global.security.api;

import com.example.runner.domain.member.entity.SocialType;
import com.example.runner.global.common.BaseResponse;
import com.example.runner.global.common.code.SuccessCode;
import com.example.runner.global.security.application.AuthService;
import com.example.runner.global.security.dto.AuthTokenReq;
import com.example.runner.global.security.dto.KakaoAuthTokenReq;
import com.example.runner.global.security.dto.KakaoAuthTokenRes;
import com.example.runner.global.security.dto.TokenDto;
import com.example.runner.global.security.exception.RefreshTokenException;
import com.example.runner.global.security.service.CustomOAuth2UserService;
import com.example.runner.global.security.service.JwtService;
import com.example.runner.global.security.user.UserSecurityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "토큰 인증 컨트롤러")
public class AuthController {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(summary = "액세스 토큰 재발급 요청하기", description = "액세스 토큰 없거나 만료됐으면 재발급 요청하기")
    @PostMapping("/token/refresh")
    public ResponseEntity<BaseResponse<TokenDto>> rotateJwtTokensRequest(
            @Valid @RequestBody AuthTokenReq authTokenReq) throws RefreshTokenException {
        TokenDto tokenDto = jwtService.rotateJwtTokens(authTokenReq.refreshToken());

        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                tokenDto
        );
    }

    @Operation(summary = "모바일 카카오 소셜로그인 확인")
    @PostMapping("/login/kakao")
    public ResponseEntity<BaseResponse<TokenDto>> mobileLoginByKakao(@RequestBody KakaoAuthTokenReq token) {
        KakaoAuthTokenRes kakaoAuthTokenRes = authService.verifyKakaoToken(token.accessToken());

        return BaseResponse.success(
                SuccessCode.SELECT_SUCCESS,
                issueToken(kakaoAuthTokenRes)
        );
    }

    private TokenDto issueToken(Object social) {
        UserSecurityDTO userSecurityDTO = customOAuth2UserService.getMobileSecurityDto(SocialType.KAKAO, social);

        return new TokenDto(
                jwtService.createAccessToken(userSecurityDTO),
                jwtService.createRefreshToken(userSecurityDTO),
                userSecurityDTO.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }
}
