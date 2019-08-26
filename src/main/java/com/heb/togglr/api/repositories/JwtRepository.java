/*
 * $Id: m228250 JwtRepository.java, v1.0 6/24/2017 12:47 PM $
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.repositories;


import com.heb.togglr.api.entities.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JwtRepository.java.
 *
 * @author m228250
 */
@Repository
public interface JwtRepository extends JpaRepository<Jwt, Long> {

    public Jwt findByJwtId(Long id);

}
