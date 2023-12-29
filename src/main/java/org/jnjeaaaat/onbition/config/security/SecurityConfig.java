package org.jnjeaaaat.onbition.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.filter.CustomAccessDeniedHandler;
import org.jnjeaaaat.onbition.config.filter.CustomAuthenticationEntryPoint;
import org.jnjeaaaat.onbition.config.filter.JwtAuthenticationFilter;
import org.jnjeaaaat.onbition.util.JwtTokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity 기본 설정
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtTokenUtil jwtTokenUtil;
  private final ObjectMapper objectMapper;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    log.info("[configure] Security configure start");
    httpSecurity.httpBasic().disable()

        .csrf().disable()

        .sessionManagement()
        .sessionCreationPolicy(
            SessionCreationPolicy.STATELESS
        )

        .and()
        .authorizeRequests()
        .antMatchers("/**/sign-up").permitAll()
        .antMatchers("/**/sign-in").permitAll()
        .antMatchers("/**/re-token").permitAll()
        .antMatchers("/**/reset").permitAll()
        .antMatchers("/**/sms/**").permitAll()

        .anyRequest().hasAnyRole("VIEWER")

        /*
        hasRole, hasAnyRole 로 권한을 걸어줘야 exceptionHandling 에서 걸러내고
        CustomAuthenticationEntryPoint 클래스에서 처리할 수 있음
         */
        .and()
        // 유저 권한 예외처리
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
        .and()
        // 토큰값에 대한 예외처리
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper))

        .and()
        .addFilterBefore(
            // uid, password, role 으로 유저 정보 처리 필터 전에
            // token 값에 대한 유효처리 필터부터 실행하라는 의미
            new JwtAuthenticationFilter(jwtTokenUtil),
            UsernamePasswordAuthenticationFilter.class)
    ;
  }

}
