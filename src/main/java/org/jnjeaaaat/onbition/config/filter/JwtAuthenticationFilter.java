package org.jnjeaaaat.onbition.config.filter;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.EXPIRED_TOKEN;
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
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.entity.token.AccessToken;
import org.jnjeaaaat.onbition.domain.repository.AccessTokenRepository;
import org.jnjeaaaat.onbition.domain.repository.RefreshTokenRepository;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
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
  private final AccessTokenRepository accessTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

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
    } catch (ExpiredJwtException e) { // accessToken 이 만료되었을때

      // refreshToken 이 만료되었을때를 생각해 try, catch 로 잡아준다.
      try {
        // refreshToken 을 uid 값으로 가져오기 위해 추출한다.
        String uid = accessTokenRepository.findByToken(token).get().getUid();
        log.info("[doFilterInternal] 만료된 토큰의 uid : {}", uid);

        // 추출한 uid 값으로 Redis 에서 refreshToken 추출
        String refreshToken = refreshTokenRepository.findByUid(uid).get().getToken();

        // refreshToken 이 만료된 토큰일때
        // ExpiredException 이 발생하면 catch 로 처리
        jwtTokenUtil.validateToken(refreshToken);

        // refreshToken 이 만료되지 않았다면 새로운 accessToken 재발급
        log.info("[doFilterInternal] accessToken 재발급 시작");

        // 토큰을 발급할 user 정보 추출
        User user = userRepository.findByUid(uid)
            .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

        // 새로운 accessToken 생성
        AccessToken Token = jwtTokenUtil.createAccessToken(user);

        // 새로 발급한 token 덮어쓰기
        accessTokenRepository.save(Token);

        // 새로 발급한 token을 responseHeader에 덮어쓰기
        jwtTokenUtil.setHeaderAccessToken(response, Token.getToken());
        log.info("[doFilterInternal] accessToken 재발급 완료 : {}", Token.getToken());

        // 작업이 끝나면 다음 filter로 넘어간다.
        filterChain.doFilter(request, response);
        return;
      } catch (ExpiredJwtException error) {
        log.error("[doFilterInternal] refreshToken 만료");
        request.setAttribute("exception", EXPIRED_TOKEN);
      } catch (NoSuchElementException error) {
        log.error("[doFilterInternal] refreshToken 만료");
        request.setAttribute("exception", INVALID_TOKEN);
      }

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
