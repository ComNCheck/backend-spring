package com.ComNCheck.ComNCheck.domain.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public CustomFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
    }

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, 
            HttpServletResponse response, 
            AuthenticationException exception
    ) throws IOException, ServletException {

        super.onAuthenticationFailure(request, response, exception);
    }
}
