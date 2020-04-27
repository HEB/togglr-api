/*
 * $Id: m228250 JwtHelper.java, v1.0 6/24/2017 10:11 PM $
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.security.jwt.service.helper;

import com.heb.togglr.api.entities.Jwt;
import com.heb.togglr.api.repositories.JwtRepository;
import com.heb.togglr.api.repositories.JwtSecretRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;

/**
 * JwtHelper.java.
 *
 * @author m228250
 */
@Component
public class JwtHelper {
    private static final Logger LOG = LoggerFactory.getLogger(JwtHelper.class);
    
    @Autowired
    private JwtRepository jwtRepository;
    @Autowired
    private JwtSecretRepository jwtSecretRepository;
    
    /**
     * Gets secret encoded.
     *
     * @param key the key
     * @return the secret encoded
     */
    public String getSecretEncoded(Long key) {
        LOG.info("*** Getting Jwt Secret");
        return Base64.getEncoder().encodeToString(
                this.jwtSecretRepository.findById(key).toString().getBytes()
        );
    }
    
    /**
     * Gets jwt by id.
     *
     * @param id the id
     * @return the jwt by id
     */
    public Jwt getJwtById(Long id) {
        LOG.info("*** Getting Jwt with ID: " + id);
        return this.jwtRepository.findByJwtId(id);
    }
    
    /**
     * Update jwt jwt.
     *
     * @param jwt the jwt
     * @return the jwt
     */
    public Jwt updateJwt(Jwt jwt) {
        return this.jwtRepository.saveAndFlush(jwt);
    }
    
}
