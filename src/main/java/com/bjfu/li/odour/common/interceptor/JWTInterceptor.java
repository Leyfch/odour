package com.bjfu.li.odour.common.interceptor;

import com.bjfu.li.odour.common.token.JWTUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Authorization");
        if (token == null)
            return false;
        try {
            JWTUtils.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
