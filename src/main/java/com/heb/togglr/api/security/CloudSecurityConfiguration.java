package com.heb.togglr.api.security;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.heb.togglr.api.security.cloud.RestAuthSuccessHandler;
import com.heb.togglr.api.security.jwt.JwtAuthenticationFilter;
import com.heb.togglr.api.security.jwt.LogoutHandler;
import com.heb.togglr.api.config.PreflightFilter;

@Order(99)
@Profile({"local","clouddev"})
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class CloudSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private LogoutHandler logutHandler;
    private RestAuthSuccessHandler restAuthSuccessHandler;
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    public CloudSecurityConfiguration(RestAuthSuccessHandler restAuthSuccessHandler,
                                      JwtAuthenticationFilter jwtAuthenticationFilter,
                                      LogoutHandler logoutHandler){
        this.logutHandler = logoutHandler;
        this.restAuthSuccessHandler = restAuthSuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     *
     * Defines the web based security configuration.
     * @param   http It allows configuring web based security for specific http requests.
     * @throws  Exception Config Exception.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors();
        http
                .formLogin()
                .loginProcessingUrl("/login")
                .successHandler(this.restAuthSuccessHandler);
        http
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(this.logutHandler);
        http
                .addFilterBefore(this.jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(this.preflightFilter(), BasicAuthenticationFilter.class);
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/features/active").permitAll()
                .antMatchers("/togglr/update").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated();
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    @Bean
    public PreflightFilter preflightFilter(){
        return new PreflightFilter();
    }
}
