package com.heb.togglr.api.security.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.heb.togglr.api.security.jwt.service.JwtService;


@Component
public class LogoutHandler implements LogoutSuccessHandler {

    @Value("${heb.togglr.jwt.tokenheader}")
    private String tokenHeader;

    @Value("${heb.togglr.app-domain}")
    private String cookieDomain;

    private JwtService jwtService;

    public LogoutHandler(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null ) {
            String authToken = httpServletRequest.getHeader(this.tokenHeader);
            this.jwtService.invalidateToken(authToken);
        }

        Cookie cookie = new Cookie(this.tokenHeader, "");
        cookie.setDomain(this.cookieDomain);
        cookie.setPath("/");
        cookie.setComment("");
        cookie.setMaxAge(0);
        httpServletResponse.addCookie(cookie);
    }
}
