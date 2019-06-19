/*
 * $Id: m228250 Jwt.java, v1.0 6/24/2017 12:47 PM $
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.entities;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Jwt.java.
 *
 * @author m228250
 */
@Entity
@Table(name = "JWT", schema = "togglr")
public class Jwt implements Serializable {
    private static final long serialVersionUID = -2244542030035151647L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "JWT_ID", nullable = false)
    private Long jwtId;
    @Column(name = "ISS_TO_UID", nullable = false)
    private String issuedTo;
    @Column(name = "LST_USE_TS", nullable = false)
    private Date lastUsed;
    @Column(name = "VLD_SW", nullable = false)
    @Type(type = "yes_no")
    private boolean valid;

    public Long getJwtId() {
        return jwtId;
    }

    public void setJwtId(Long jwtId) {
        this.jwtId = jwtId;
    }

    public String getIssuedTo() {
        return issuedTo;
    }

    public void setIssuedTo(String issuedTo) {
        this.issuedTo = issuedTo;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jwt jwt = (Jwt) o;
        return valid == jwt.valid &&
                Objects.equals(jwtId, jwt.jwtId) &&
                Objects.equals(issuedTo, jwt.issuedTo) &&
                Objects.equals(lastUsed, jwt.lastUsed);
    }

    @Override
    public int hashCode() {

        return Objects.hash(jwtId, issuedTo, lastUsed, valid);
    }
}
