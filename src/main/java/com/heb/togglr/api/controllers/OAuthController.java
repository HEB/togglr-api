package com.heb.togglr.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heb.togglr.api.constants.TogglrApiConstants;
import com.heb.togglr.api.entities.SuperAdminsEntity;
import com.heb.togglr.api.repositories.SuperAdminRepository;
import com.heb.togglr.api.security.jwt.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class OAuthController {
    @Value("${spring.security.oauth2.client.userAuthorizationUri:null}")
    private String userAuthorizationUri;

    @Value("${spring.security.oauth2.client.clientSecret:#{null}}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.clientId:#{null}}")
    private String clientId;

    @Value("${spring.security.oauth2.client.accessTokenUri:#{null}}")
    private String accessTokenUri;

    @Value("${spring.security.oauth2.client.redirectUri:#{null}}")
    private String redirectUri;

    @Value("${heb.togglr.app-domain}")
    private String cookieDomain;

    @Value("${heb.togglr.oauth.tokenheader}")
    private String oauthTokenHeader;

    @Value("${heb.togglr.jwt.tokenheader}")
    private String tokenHeader;

    @Value("${heb.togglr.oauth.userIdentification}")
    private String oauthUserIdentification;

    @Value("${spring.security.oauth2.resource.userInfoUri}")
    private String userInfoUri;


    @Autowired
    private JwtService jwtService;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    private static Logger logger = LoggerFactory.getLogger(OAuthController.class);

    private RestTemplate restTemplate = new RestTemplate();;

    @RequestMapping(method = RequestMethod.GET, value = "/oauth/login")
    @ResponseBody
    public ResponseEntity<?> getOAuthLoginUrl() {
        StringBuilder oauthUrl = new StringBuilder();

        // will cause a blank to be returned if user did not configure the oauth docker env variables or if they aren't present inside docker-compose
        if (!this.userAuthorizationUri.equalsIgnoreCase("") && !this.clientId.equalsIgnoreCase("")) {
            oauthUrl.append(this.userAuthorizationUri)
                    .append("client_id=")
                    .append(this.clientId);
        }

        return ResponseEntity.ok(oauthUrl.toString());
    }

    @RequestMapping(method = RequestMethod.GET, value="/oauth/signin/callback")
    @ResponseBody
    public void handleOAuthTemporaryCode(@RequestParam() String code, HttpServletResponse servletResponse) throws IOException {

            // preparing  POST request uri to provider to get access token
            StringBuilder accessTokenRequestUri = new StringBuilder();
            accessTokenRequestUri.append(this.accessTokenUri)
                    .append("?client_id=").append(this.clientId)
                    .append("&client_secret=").append(this.clientSecret)
                    .append("&code=").append(code);

            // setting the 'Accept': 'Application/Json' header so provider returns data in JSON format
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            // making POST to get access token
            ResponseEntity<String> response = this.restTemplate.postForEntity(
                    accessTokenRequestUri.toString(),
                    entity,
                    String.class);

            // Now lets prepare to parse the json data that came back
            ObjectMapper objectMapper = new ObjectMapper();
            // parse the response into a jsonnode
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            // try to get the access_token if it's present
            String token = jsonNode.get("access_token").asText(null);

            // GET user info from oauth provider
            headers.setBearerAuth(token);
            entity = new HttpEntity<>(null, headers);
            ResponseEntity<String> userResponse = this.restTemplate.exchange(
                    this.userInfoUri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            jsonNode = objectMapper.readTree(userResponse.getBody());
            String userIdentifier = jsonNode.get(this.oauthUserIdentification).asText(null);

            // determine if superadmin and add that role to the User
            SuperAdminsEntity superAdmin = this.superAdminRepository.findById(userIdentifier).orElse(null);
            List<GrantedAuthority> userRoles = new ArrayList<>();
            if (superAdmin != null) {
                userRoles.add(new SimpleGrantedAuthority(TogglrApiConstants.ROLE_PREFIX + TogglrApiConstants.SUPERADMIN_ROLE_NAME));
            }

            User userDetails = new User(userIdentifier, "", true, true, true, true, userRoles);

            String jwt = this.jwtService.generateToken(userDetails);

            Cookie oauthToken = new Cookie(this.oauthTokenHeader, token);
            oauthToken.setDomain(this.cookieDomain);
            oauthToken.setPath("/");

            Cookie togglrToken = new Cookie(this.tokenHeader, jwt);
            togglrToken.setDomain(this.cookieDomain);
            togglrToken.setPath("/");

            servletResponse.addCookie(togglrToken);
            servletResponse.addCookie(oauthToken);

            servletResponse.sendRedirect(this.redirectUri);

    }

}
