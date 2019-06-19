/*
 * $Id: CacheConfiguration.java,v 1.1 3/11/2016 9:38 AM b594249 Exp $
 *
 * Copyright (c) 2013 HEB
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of HEB.
 */
package com.heb.togglr.api.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * The Application cache configuration.
 *
 * @author b594249
 */
@Configuration
@EnableCaching
public class AppCacheConfig {

    public static final String SECRETS = "secrets";
    public static final String JWT = "jwt";

    /**
     * Eh cache cache manager eh cache cache manager.
     *
     * @return the eh cache cache manager
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }

    /**
     * Eh cache manager factory bean eh cache manager factory bean.
     *
     * @return the eh cache manager factory bean
     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }
}
