/*
 * $Id: m228250 JwtSecretRepository.java, v1.0 6/24/2017 12:53 PM $
 *
 * Copyright (c) 2017 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.repositories;

import com.heb.togglr.api.entities.JwtSecret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JwtSecretRepository.java.
 *
 * @author m228250
 */
@Repository
public interface JwtSecretRepository extends JpaRepository<JwtSecret, Long> {
}
