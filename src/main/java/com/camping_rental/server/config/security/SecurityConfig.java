package com.camping_rental.server.config.security;

import com.camping_rental.server.config.auth.JwtAuthorizationFilter;
import com.camping_rental.server.config.csrf.CsrfAuthenticationFilter;
import com.camping_rental.server.config.csrf.CsrfExceptionFilter;
import com.camping_rental.server.config.referer.RefererAuthenticationFilter;
import com.camping_rental.server.config.referer.RefererExceptionFilter;
import com.camping_rental.server.domain.refresh_token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);    // 토큰을 활용하면 세션이 필요없으므로 STATELESS로 설정해 세션을 사용하지 않는다

        http.cors();
        http
                .authorizeRequests()
                .anyRequest().permitAll()
        ;
        http
                .addFilterBefore(new RefererAuthenticationFilter(), CsrfFilter.class)
                .addFilterAfter(new CsrfAuthenticationFilter(), RefererAuthenticationFilter.class)
                .addFilterAfter(new JwtAuthorizationFilter(refreshTokenRepository), CsrfAuthenticationFilter.class)
                .addFilterBefore(new RefererExceptionFilter(), RefererAuthenticationFilter.class)
                .addFilterBefore(new CsrfExceptionFilter(), CsrfAuthenticationFilter.class)
//                .addFilterBefore(new JwtAuthorizationExceptionFilter(), JwtAuthorizationFilter.class)
        ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 setting 시에는 시큐리티를 무시한다.
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
        web.httpFirewall(defaultHttpFirewall());
    }

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }
}
