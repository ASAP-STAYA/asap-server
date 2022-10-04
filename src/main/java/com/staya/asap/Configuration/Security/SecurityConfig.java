package com.staya.asap.Configuration.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 해당 메서드의 리턴되는 오브젝트를 IoC로 등록해준다.
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/api/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .usernameParameter("email")
                .loginProcessingUrl("/api/auth/signin");
    }
}
