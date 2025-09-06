package com.ComNCheck.ComNCheck.domain.security.filter;

import com.ComNCheck.ComNCheck.domain.member.model.dto.response.MemberDTO;
import com.ComNCheck.ComNCheck.domain.member.model.entity.Role;
import com.ComNCheck.ComNCheck.domain.security.auth.CustomUserDetails;
import com.ComNCheck.ComNCheck.domain.security.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

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
//            "/api/v1/**"
            "/api/v1/member/login"
    };

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return Arrays.stream(EXCLUDED_PATHS)
                .anyMatch(p -> pathMatcher.match(p, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

//        String token = null;
//        Cookie[] cookies = request.getCookies();
//
//        String path = request.getRequestURI();
//        System.out.println("path: " + path);
//        for(Cookie cookie : cookies){
//            System.out.println("cookie: " + cookie);
//        }
//
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("AccessToken".equals(cookie.getName())) {
//                    token = cookie.getValue();
//                    logger.debug("AccessToken 쿠키를 찾았습니다: " + token);
//                    break;
//                }
//            }
//        }

        //앱의 경우 헤더에서, 웹의 경우 쿠키에서 토큰 가져옴(둘다 없으면 null 리턴)
        String token = resolveToken(request);
        System.out.println("token: " + token);
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


            CustomUserDetails customUserDetails = new CustomUserDetails(memberDTO);

            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
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

    private String resolveToken(HttpServletRequest request) {

        // 1. "앱은 api 통신으로 주고받고" -> Authorization 헤더 확인
        // 앱에서 보낸 요청은 여기서 토큰을 찾고 바로 반환됩니다.
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            System.out.println("test1");
            return bearerToken.substring(7);
        }

        // 2. "웹은 쿠키로 통신하잖아요" -> 쿠키 확인
        // 웹에서 보낸 요청은 위 if문을 통과하고, 여기서 토큰을 찾게 됩니다.
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AccessToken".equals(cookie.getName())) {
                    System.out.println("test2");

                    return cookie.getValue();
                }
            }
        }

        // 3. "쿠키가 있을수도 없을수도 있는게" -> 둘 다 없는 경우
        // 로그인 전 사용자의 요청은 위 두 로직을 모두 통과하고, 최종적으로 null을 반환합니다.
        System.out.println("test3");
        return null;
    }
}
