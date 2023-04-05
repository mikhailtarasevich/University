package com.mikhail.tarasevich.university.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

@Configuration
@Import(PersistenceConfig.class)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String DELETE = "DELETE";
    private static final String WRITE = "WRITE";

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new HiddenHttpMethodFilter(), ChannelProcessingFilter.class)
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/about").permitAll()
                .antMatchers("/resources/img/**").permitAll()
                .antMatchers(HttpMethod.DELETE).hasAuthority(DELETE)
                .antMatchers(HttpMethod.POST).hasAuthority(WRITE)
                .antMatchers(HttpMethod.PATCH).hasAuthority(WRITE)
                .antMatchers(HttpMethod.PUT).hasAuthority(WRITE)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

}
