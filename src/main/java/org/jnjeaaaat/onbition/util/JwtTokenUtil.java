package org.jnjeaaaat.onbition.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * AccessToken, RefreshToken 생성
 * 토큰으로부터 uid, user PK 추출
 * 토큰 유효성 체크 기능을 제공하는 Util Class
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

  private final UserDetailsService userDetailsService;

  @Value("${jwt.token.secret}")
  private String secretKey;

  @Value("${jwt.token.header}")
  private String jwtHeader;

  private final long atkExpiredTime = Duration.ofMinutes(30).toMillis(); // 만료기간 30분
  private final long rtkExpiredTime = Duration.ofDays(14).toMillis(); // 만료기간 2주

  @PostConstruct
  protected void init() {
    log.info("[init] JwtTokenProvider 내 secretKey 초기화 시작");
    secretKey = Base64
        .getEncoder()
        .encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));

    log.info("[init] JwtTokenProvider 내 secretKey 초기화 완료");
  }

  /*
  accessToken 생성
   */
  public String createAccessToken(User user) {
    return createToken(user, atkExpiredTime);
  }

  /*
  refreshToken 생성
   */
  public String createRefreshToken(User user) {
    return createToken(user, rtkExpiredTime);
  }
  /*
  user info 값으로 accessToken 발행
  @return String
   */
  private String createToken(User user, Long expiredTimeMs) {
    log.info("[createToken] 토큰 생성 시작");
    Claims claims = Jwts.claims()
        // uid 저장
        .setSubject(user.getUid());
    claims.put("userId", user.getId());  // 일종의 map
    claims.put("roles", user.getRoles());

    Date now = new Date(System.currentTimeMillis());

    String token = Jwts.builder()       // 토큰 생성
        .setClaims(claims)
        .setIssuedAt(now)      //  시작 시간 : 현재 시간기준으로 만들어짐
        .setExpiration(new Date(
            System.currentTimeMillis() + expiredTimeMs))     // 끝나는 시간 : 지금 시간 + 유지할 시간(입력받아옴)
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();

    log.info("[createToken] 토큰 생성 완료");

    return token;
  }

  /*
  토큰 인증 정보 조회
   */
  public Authentication getAuthentication(String token) {
    log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
    UserDetails userDetails =
        userDetailsService.loadUserByUsername(
            // claim 의 subject 에 저장되어있는 uid
            getUsername(token)
        );
    log.info("[getAuthentication] 토큰 인증 정보 조회 완료, UserDetails Username : {}",
        userDetails.getUsername());
    return new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities()
    );
  }

  /*
  token 으로 uid 추출
   */
  public String getUsername(String token) {
    log.info("[getUsername] 토큰 기반 회원 email 추출");
    String info = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token) // Jws<Claims>
        .getBody() // Claims
        .getSubject(); // 암호화 되어있는 uid 값
    log.info("[getUsername] 토큰 기반 회원 uid 추출 완료, info : {}", info);

    return info;
  }

  /*
  헤더로부터 token 추출
   */
  public String resolveToken(HttpServletRequest request) {
    log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
    return request.getHeader(jwtHeader);
  }

  /*
  토큰 유효성 체크
   */
  public boolean validateToken(String token) {
    log.info("[validateToken] 토큰 유효 체크 시작");

    Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

    return !claims.getBody().getExpiration().before(new Date());
  }

  /*
  token 으로 부터 userId 값 가져오기
   */
  public Long getUserIdFromToken() {
    log.info("[getUserIdFromToken] userId 값 가져오기");
    HttpServletRequest request =
        ((ServletRequestAttributes)
            RequestContextHolder.currentRequestAttributes())
            .getRequest();
    String token = resolveToken(request);

    log.info("token : {}", token);

    return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token) // Jws<Claims>
        .getBody() // Claims
        .get("userId", Long.class); // userId 값
  }

  /*
  토큰의 남은 만료시간 추출
   */
  public Long getExpiration(String token) {
    // accessToken 남은 유효시간
    Date expiration = Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody()
        .getExpiration();
    // 현재 시간
    Long now = new Date().getTime();
    return (expiration.getTime() - now);
  }

}
