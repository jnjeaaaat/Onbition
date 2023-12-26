package org.jnjeaaaat.onbition.config.filter;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.ACCESS_TOKEN_EXPIRED_TOKEN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.INVALID_TOKEN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.NoSuchElementException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.util.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * JwtToken 유효성 관리 Filter
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;

  /*
  Filter 에 진입하면 실행하게 될 doFilter()
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    // 헤더로부터 accessToken 을 추출
    String token = jwtTokenUtil.resolveToken(request);
    log.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

    log.info("[doFilterInternal] token 값 유효성 체크 시작");
    try {
      if (token != null && jwtTokenUtil.validateToken(token)) {  // token 이 null 인지 만료되지는 않았는지 확인
        Authentication authentication = jwtTokenUtil.getAuthentication(token); // 만료되지 않았다면 권한 목록 추출
        SecurityContextHolder.getContext().setAuthentication(authentication); // SpringSecurity 에 권한 목록 넘겨줌
        log.info("[doFilterInternal] token 값 유효성 체크 완료");
      }
    } catch (ExpiredJwtException e) {
      // accessToken 이 만료되었을때
      log.error("[doFilterInternal] accessToken 만료");

      request.setAttribute("exception", ACCESS_TOKEN_EXPIRED_TOKEN);
    } catch (JwtException | IllegalArgumentException e) {
      // 토큰의 타입이 잘못된 타입이면,
      log.error("[doFilterInternal] 잘못된 타입의 토큰 에러");

      request.setAttribute("exception", INVALID_TOKEN);
    } catch (NoSuchElementException e) {
      log.error("[doFilterInternal] 유효하지 않은 사용자 에러");

      request.setAttribute("exception", NOT_FOUND_USER);
    }

    filterChain.doFilter(request, response);

  }

}
