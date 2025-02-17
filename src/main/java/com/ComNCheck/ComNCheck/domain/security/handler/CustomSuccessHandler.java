package com.ComNCheck.ComNCheck.domain.security.handler;

import com.ComNCheck.ComNCheck.domain.security.oauth.CustomOAuth2Member;
import com.ComNCheck.ComNCheck.domain.security.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2Member customMemberDetails = (CustomOAuth2Member) authentication.getPrincipal();

        Long memberId = customMemberDetails.getMemberDTO().getMemberId();
        String username = customMemberDetails.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority().toString();
        boolean checkStudentCard = customMemberDetails.isCheckStudentCard();
        System.out.println(checkStudentCard);

        String token = jwtUtil.createJwt(memberId, username, role,  365L * 24 * 60 * 60 * 1000); // 60 * 60 * 1000L

        response.addCookie(createCookie("AccessToken", token));
        if(!checkStudentCard) {
            response.sendRedirect("https://com-n-check.vercel.app/login/first"); //https://com-n-check.vercel.app
        }
        else {
            response.sendRedirect("https://com-n-check.vercel.app/notice");
        }
        //response.sendRedirect("http://localhost:3000/login/first");

    }

    public void clearAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        Cookie jsessionCookie = new Cookie("JSESSIONID", null);
        jsessionCookie.setPath("/");
        jsessionCookie.setMaxAge(0);
        response.addCookie(jsessionCookie);

        Cookie accessTokenCookie = new Cookie("AccessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setSecure(true); // https에서만 작동
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}

