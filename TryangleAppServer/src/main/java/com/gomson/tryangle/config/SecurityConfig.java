package com.gomson.tryangle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/static/js/**", "/img/**", "/lib/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests()
////                .antMatchers("/login").permitAll()
////                .antMatchers("/admin/**").hasRole("Admin")
////                .anyRequest().authenticated()
//                .anyRequest().permitAll()
//                .and().formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/")
//                .invalidateHttpSession(true)
//                .and()
//                .csrf()
//                .csrfTokenRepository(new CookieCsrfTokenRepository())
//                .and()
//                .exceptionHandling().accessDeniedPage("/denied");
    }
}
