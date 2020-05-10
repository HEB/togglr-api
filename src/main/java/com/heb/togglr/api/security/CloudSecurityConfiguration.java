package com.heb.togglr.api.security;


import com.heb.togglr.api.config.PreflightFilter;
import com.heb.togglr.api.security.cloud.RestAuthFailureHandler;
import com.heb.togglr.api.security.cloud.RestAuthSuccessHandler;
import com.heb.togglr.api.security.jwt.JwtAuthenticationFilter;
import com.heb.togglr.api.security.jwt.LogoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Order(99)
@Profile({"local","clouddev"})
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
public class CloudSecurityConfiguration extends ResourceServerConfigurerAdapter {


    @Value("${spring.security.oauth2.client.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.resource.userInfoUri}")
    private String userInfoEndPointUri;

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

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("togglr-api");
    }

    @Primary
    @Bean
    public UserInfoTokenServices tokenService() {
        final UserInfoTokenServices tokenServices = new UserInfoTokenServices(this.userInfoEndPointUri, this.clientId);
        return tokenServices;
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
                .successHandler(this.restAuthSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler());
        http
                .logout().logoutUrl("/logout")
                .logoutSuccessHandler(this.logutHandler);
        http
                .addFilterBefore(this.jwtAuthenticationFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(this.preflightFilter(), BasicAuthenticationFilter.class);
        http
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/oauth/login").permitAll()
                .antMatchers("/oauth/signin/callback").permitAll()
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

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new RestAuthFailureHandler();
    }
}
