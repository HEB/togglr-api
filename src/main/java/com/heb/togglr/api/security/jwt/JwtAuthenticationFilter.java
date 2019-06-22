/*
 * $Id: StatelessAuthenticationFilter.java,v 1.1 6/17/2016 1:23 PM b594249 Exp $
 *
 * Copyright (c) 2013 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.security.jwt;

import com.heb.togglr.api.client.TogglrClient;
import com.heb.togglr.api.client.model.requests.ActiveFeaturesRequest;
import com.heb.togglr.api.security.jwt.service.JwtService;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author m228250
 */
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Value("${heb.togglr.jwt.tokenheader}")
    private String tokenHeader;

    @Value("${heb.togglr.app-domain}")
    private String cookieDomain;

    @Value("${spring.security.user.name}")
    private String username;

    private JwtService jwtService;
    private TogglrClient togglrClient;

    public JwtAuthenticationFilter(JwtService jwtService, TogglrClient togglrClient){
        this.jwtService = jwtService;
        this.togglrClient = togglrClient;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        try {
            String authToken = httpRequest.getHeader(this.tokenHeader);
            if(authToken == null){
                throw new JwtException("No token present");
            }
            String username = this.jwtService.getUserIdFromToken(authToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (this.jwtService.isValidToken(authToken, true)) {
                    UserDetails user = this.jwtService.getUserFromToken(authToken);

                    ActiveFeaturesRequest featuresRequest = new ActiveFeaturesRequest();
                    featuresRequest.getConfigs().put("Username", this.username);

                    logger.trace(featuresRequest.toString());

                    List<GrantedAuthority> roles = this.togglrClient.getFeaturesForConfig(featuresRequest, this.username);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, authToken, roles);
                    authentication.setDetails(user);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    String jwt = this.jwtService.generateToken(user);

                    Cookie cookie = new Cookie(this.tokenHeader, jwt);
                    cookie.setDomain(this.cookieDomain);
                    cookie.setPath("/");
                    cookie.setComment("");
                    ((HttpServletResponse)response).addCookie(cookie);

                } else {
                    LOGGER.error("INVALID TOKEN FROM USER: " + username);
                    throw new JwtException("Invalid token");
                }
            }
        }catch (JwtException jwtException){
            HttpServletResponse httpServletResponse = (HttpServletResponse)response;
            Cookie cookie = new Cookie(this.tokenHeader, "");
            cookie.setDomain(this.cookieDomain);
            cookie.setPath("/");
            cookie.setComment("");
            cookie.setMaxAge(0);
            httpServletResponse.addCookie(cookie);
        }

        filterChain.doFilter(request, response);
    }
}
