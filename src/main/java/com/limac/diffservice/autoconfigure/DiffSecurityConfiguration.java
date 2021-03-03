package com.limac.diffservice.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * Configuration class to define authorization configurations.
 */
@Configuration
@EnableWebSecurity
public class DiffSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${security.basic.user:user}")
    private String user;

    @Value("${security.basic.pass:pass}")
    private String pass;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.inMemoryAuthentication()
            .withUser(user).password(passwordEncoder().encode(pass))
            .authorities("ROLE_USER");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf().disable()
            .authorizeRequests().anyRequest().fullyAuthenticated().and().httpBasic();
    }

    /**
     * Defines a {@link BCryptPasswordEncoder} as the password encoder bean.
     *
     * @return {@link BCryptPasswordEncoder} bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
