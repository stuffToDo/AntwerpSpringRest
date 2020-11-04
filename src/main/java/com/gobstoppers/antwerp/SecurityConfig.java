package com.gobstoppers.antwerp;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(auths -> auths
                .antMatchers(HttpMethod.GET).hasAuthority("SCOPE_antwerp_read")
                .antMatchers(HttpMethod.POST).hasAuthority("SCOPE_antwerp_write")
                .antMatchers(HttpMethod.PUT).hasAuthority("SCOPE_antwerp_write")
                .antMatchers(HttpMethod.DELETE).hasAuthority("SCOPE_antwerp_write")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
    }
}
