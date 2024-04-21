package com.example.runner.global.security.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {
    /**
     * 로그아웃
     * 로그아웃은 CSRF 공격을 막기 위해 POST 메서드를 사용
     * SecurityContextLogoutHandler 은 현재 존재하는 HttpSession 객체를 비활성화하고 SecurityContext에서 인증 정보를 삭제하면서 사용자를 로그아웃 처리한다.
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/doLogout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "redirect:/login";
    }
}
