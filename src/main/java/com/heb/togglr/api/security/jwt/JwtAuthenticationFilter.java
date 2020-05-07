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

import com.heb.togglr.api.constants.TogglrApiConstants;
import com.heb.togglr.api.entities.SuperAdminsEntity;
import com.heb.togglr.api.repositories.SuperAdminRepository;
import com.heb.togglr.api.security.jwt.service.JwtService;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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
import java.util.ArrayList;
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

    private JwtService jwtService;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService = jwtService;
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

                    SuperAdminsEntity superAdmin = superAdminRepository.findById(username).orElse(null);
                    List<GrantedAuthority> userRoles = new ArrayList<>();
                    if (superAdmin != null) {
                        userRoles.add(new SimpleGrantedAuthority(TogglrApiConstants.ROLE_PREFIX + TogglrApiConstants.SUPERADMIN_ROLE_NAME));
                    }

                    UserDetails user = new User(username, "", true, true, true, true, userRoles);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, authToken, new ArrayList<>());
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
