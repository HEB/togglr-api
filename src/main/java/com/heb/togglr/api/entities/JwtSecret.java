/*
 * $Id: m228250 JwtSecret.java, v1.0 6/24/2017 12:47 PM $
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * JwtSecret.java.
 *
 * @author m228250
 */
@Entity
@Table(name = "JWT_SECRET", schema = "togglr")
public class JwtSecret implements Serializable {
    private static final long serialVersionUID = -9080798235176314398L;
    
    @Id
    @Column(name = "JWT_SECRET_KEY")
    private Long secretKey;
    @Column(name = "JWT_SECRET_64")
    private String secret64;
    
    public Long getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(Long secretKey) {
        this.secretKey = secretKey;
    }
    
    public String getSecret64() {
        return secret64;
    }
    
    public void setSecret64(String secret64) {
        this.secret64 = secret64;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        JwtSecret jwtSecret = (JwtSecret) o;
    
        return secretKey.equals(jwtSecret.secretKey);
    
    }
    
    @Override
    public int hashCode() {
        return secretKey.hashCode();
    }
}
