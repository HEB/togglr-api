package com.heb.togglr.api.security.cloud;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.heb.togglr.api.client.TogglrClient;
import com.heb.togglr.api.client.model.requests.ActiveFeaturesRequest;
import com.heb.togglr.api.security.jwt.service.JwtService;

@Component
@Profile({"local","clouddev"})
public class RestAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${heb.togglr.jwt.tokenheader}")
    private String tokenHeader;

    @Value("${heb.togglr.app-domain}")
    private String cookieDomain;

    @Value("${heb.togglr.client.app-id}")
    private Integer togglrAppId;

    @Value("${spring.security.user.name}")
    private String username;

    private JwtService jwtService;
    private TogglrClient togglrClient;


    public RestAuthSuccessHandler(JwtService jwtService, TogglrClient togglrClient){
        this.jwtService = jwtService;
        this.togglrClient = togglrClient;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        ActiveFeaturesRequest featuresRequest = new ActiveFeaturesRequest();
        featuresRequest.setAppId(this.togglrAppId);
        featuresRequest.getConfigs().put("username", this.username);

        List<GrantedAuthority> roles = this.togglrClient.getFeaturesForConfig(featuresRequest, this.username);

        User userDetails = new User(this.username, "",  true, true, true, true, roles);

        String jwt = this.jwtService.generateToken(userDetails);

        Cookie cookie = new Cookie(this.tokenHeader, jwt);
        cookie.setDomain(this.cookieDomain);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
