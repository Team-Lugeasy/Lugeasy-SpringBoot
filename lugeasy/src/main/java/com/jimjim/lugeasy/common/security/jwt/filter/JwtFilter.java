package com.jimjim.lugeasy.common.security.jwt.filter;

import com.jimjim.lugeasy.common.error.RestApiException;
import com.jimjim.lugeasy.common.security.jwt.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7); // ✅ "Bearer " 제거하고 토큰 추출

        try {
            // ✅ 토큰 만료 확인
            if (jwtService.isExpired(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 반환
                response.getWriter().write("Token is expired or invalid.");
                return;
            }

            // ✅ 토큰이 유효하면 사용자 인증 설정
            Authentication authentication = jwtService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RestApiException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 반환
            response.getWriter().write("Token error: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
