package org.jnjeaaaat.onbition.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.filter.CustomAccessDeniedHandler;
import org.jnjeaaaat.onbition.config.filter.CustomAuthenticationEntryPoint;
import org.jnjeaaaat.onbition.config.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity 기본 설정
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    log.info("[configure] Security configure start");
    return httpSecurity.httpBasic(AbstractHttpConfigurer::disable)

        .csrf(AbstractHttpConfigurer::disable)

        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(
                "/api/v1/sign-up",
                "/api/v1/sign-in",
                "/api/v1/re-token",
                "/api/v1/reset",
                "/api/v1/sms/**"
            ).permitAll()

            .requestMatchers(
                "/api/v1/payments/{paintingId}"
            ).hasAnyRole("BUYER")

            .anyRequest().authenticated()
        )

        /*
        hasRole, hasAnyRole 로 권한을 걸어줘야 exceptionHandling 에서 걸러내고
        CustomAuthenticationEntryPoint 클래스에서 처리할 수 있음
         */
        // 유저 권한 예외처리
        .exceptionHandling(configurer -> {
          configurer.authenticationEntryPoint(customAuthenticationEntryPoint);
          configurer.accessDeniedHandler(customAccessDeniedHandler);
        })

        .addFilterBefore(
            // uid, password, role 으로 유저 정보 처리 필터 전에
            // token 값에 대한 유효처리 필터부터 실행하라는 의미
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .build()
        ;
  }

}
