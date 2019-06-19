/*
 * $Id: m228250 JwtService.java, v1.0 6/24/2017 12:47 PM $
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.security.jwt.service;

import com.heb.togglr.api.entities.Jwt;
import com.heb.togglr.api.repositories.JwtRepository;
import com.heb.togglr.api.security.jwt.service.helper.JwtHelper;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * JwtService.java.
 *
 * @author m228250
 */
@Service
public class JwtService {
    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);
    
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private JwtRepository jwtRepository;

    private static final String USER_KEY = "details";

    @Value("${heb.togglr.jwt.secret.id}")
    private Long secretId;
    @Value("${heb.togglr.jwt.session.seconds}")
    private int sessionExpirationSeconds;


    /**
     * Generate a token string. Expiration is one year. The subject contains the user id
     *
     * @param user   the user
     * @return the string
     */
    public String generateToken(UserDetails user){
        com.heb.togglr.api.entities.Jwt jwt = this.saveNewJwt(user.getUsername().toLowerCase());
        Claims claims = Jwts.claims();

        claims.put(USER_KEY, user);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, this.jwtHelper.getSecretEncoded(this.secretId))
                .setId(jwt.getJwtId().toString())
                .setIssuedAt(jwt.getLastUsed())
                .setSubject(user.getUsername())
                .compact();
    }

    /**
     * Invalidate token.
     *
     * @param token the token
     */
    public void invalidateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(this.jwtHelper.getSecretEncoded(this.secretId))
                    .parseClaimsJws(token).getBody();
            com.heb.togglr.api.entities.Jwt jwt = this.jwtHelper.getJwtById(Long.parseLong(claims.getId()));
            jwt.setValid(Boolean.FALSE);
            this.jwtRepository.saveAndFlush(jwt);
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException ignore) {
            LOG.error("Error invalidating JWT Token.");
        }
    }
    
    /**
     * Is valid token boolean.
     *
     * @param token       the token
     * @param logNewUsage the log new usage
     * @return the boolean
     */
    public boolean isValidToken(String token, boolean logNewUsage) {
        if(token == null || token.equals("")){
            return false;
        }

        boolean isValid = false;
        try {
            Claims claims = Jwts.parser().setSigningKey(this.jwtHelper.getSecretEncoded(this.secretId))
                    .parseClaimsJws(token).getBody();
            
            com.heb.togglr.api.entities.Jwt jwt = this.jwtHelper.getJwtById(Long.parseLong(claims.getId()));

            if(jwt == null){
                return false;
            }

            isValid = jwt.isValid() &&
                    !this.isTokenExpired(jwt) &&
                    claims.getSubject() != null;
            
            if (isValid && logNewUsage) {
                jwt.setLastUsed(new Date());
                this.jwtHelper.updateJwt(jwt);
            }
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException ignore) {
            LOG.error("Error checking if JWT Token is valid.");
        }
        return isValid;
    }
    
    /**
     * Saves new jwt to the database.
     *
     * @param issuedTo the issued to
     * @return the saved jwt
     */
    private com.heb.togglr.api.entities.Jwt saveNewJwt(String issuedTo) {
        com.heb.togglr.api.entities.Jwt jwt = new com.heb.togglr.api.entities.Jwt();
        jwt.setIssuedTo(issuedTo);
        jwt.setLastUsed(new Date());
        jwt.setValid(Boolean.TRUE);
        return this.jwtRepository.saveAndFlush(jwt);
    }
    
    /**
     * Is token expired boolean.
     *
     * @param jwt the jwt
     * @return the boolean
     */
    private boolean isTokenExpired(Jwt jwt) {
        Date jwtLastUsed = jwt.getLastUsed();
        Date expirationDate = this.getExpirationForNewToken(-this.sessionExpirationSeconds);
        return jwtLastUsed == null || expirationDate.after(jwtLastUsed);
    }
    
    /**
     * Gets user id from token.
     *
     * @param token the token
     * @return the user id from token
     */
    public String getUserIdFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(this.jwtHelper.getSecretEncoded(this.secretId))
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (IllegalArgumentException | ExpiredJwtException | MalformedJwtException ignore) {
            LOG.error("Error parsing user id from JWT token");
        }
        return null;
    }

    public UserDetails getUserFromToken(String token){
        Object userObject =  Jwts.parser().setSigningKey(this.jwtHelper.getSecretEncoded(this.secretId))
                .parseClaimsJws(token)
                .getBody()
                .get(USER_KEY);

        UserDetails userDetails = this.mapUserFromHashMap((HashMap) userObject);

        return userDetails;
    }

    /**
     * Gets expiration for new token.
     *
     * @param seconds the seconds
     * @return the expiration for new token
     */
    private Date getExpirationForNewToken(int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * Map the user from the JWT token.
     * @param map JWT decoded values
     * @return User
     */
    private UserDetails mapUserFromHashMap(HashMap map){
        return new User(
                map.get("username").toString(),
                map.get("password").toString(),
                Boolean.parseBoolean(map.get("accountNonExpired").toString()),
                Boolean.parseBoolean(map.get("accountNonExpired").toString()),
                Boolean.parseBoolean(map.get("accountNonExpired").toString()),
                Boolean.parseBoolean(map.get("accountNonExpired").toString()),
                new LinkedList<>()
        );
    }
}