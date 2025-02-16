package com.ComNCheck.ComNCheck.domain.security.filter;

import com.ComNCheck.ComNCheck.domain.member.model.dto.response.MemberDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import com.ComNCheck.ComNCheck.domain.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    private static final String[] EXCLUDED_PATHS = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/V3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/login/**",
            "/oauth2/**",
            "/h2-console/**",
            "api/v1/**"
    };
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        for (String excludedPath : EXCLUDED_PATHS) {
            if (pathMatcher.match(excludedPath, path)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AccessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    logger.debug("AccessToken 쿠키를 찾았습니다: " + token);
                    break;
                }
            }
        }

        try {
            if (token == null || token.trim().isEmpty()) {
                logger.error("AccessToken 쿠키가 존재하지 않습니다.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\": \"MISSING_TOKEN\", \"message\": \"AccessToken 쿠키가 존재하지 않습니다.\"}");
                response.getWriter().flush();
                return;
            }

            String username = jwtUtil.getUsername(token);
            Long id = jwtUtil.getId(token);
            Role role = jwtUtil.getRole(token);

            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberId(id);
            memberDTO.setName(username);
            memberDTO.setRole(role);

            CustomOAuth2Member customOAuth2Member = new CustomOAuth2Member(memberDTO);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customOAuth2Member,
                    null,
                    customOAuth2Member.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
            logger.debug("SecurityContext에 사용자 정보를 설정했습니다: " + username);

        } catch (ExpiredJwtException e) {
            logger.error("토큰 만료: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"TOKEN_EXPIRED\", \"message\": \"" + e.getMessage() + "\"}");
            response.getWriter().flush();
            return;
        } catch (Exception e) {
            logger.error("인증 오류: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"AUTHENTICATION_ERROR\", \"message\": \"" + e.getMessage() + "\"}");
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
