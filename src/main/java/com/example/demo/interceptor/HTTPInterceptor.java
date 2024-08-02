package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class HTTPInterceptor implements HandlerInterceptor {

    @Value("${api.key.name}")
    private String apiKeyName;

    @Value("${api.key.value}")
    private String apiKeyValue;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestAPIKey = request.getHeader(apiKeyName);
        if (apiKeyValue.equals(requestAPIKey)) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
            return false;
        }
    }
}
