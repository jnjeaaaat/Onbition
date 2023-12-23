package org.jnjeaaaat.onbition.service.impl.auth;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.ALREADY_LOGOUT;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.ALREADY_REGISTERED_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.DUPLICATED_USER_NAME;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.INVALID_TOKEN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_TOKEN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.REFRESH_TOKEN_EXPIRED_TOKEN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_PASSWORD;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.auth.ReissueResponse;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInResponse;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.entity.token.Token;
import org.jnjeaaaat.onbition.domain.repository.TokenRepository;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.ImageService;
import org.jnjeaaaat.onbition.service.SignService;
import org.jnjeaaaat.onbition.service.TokenService;
import org.jnjeaaaat.onbition.util.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 회원가입, 로그인 service interface 구현체 class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SignServiceImpl implements SignService {

  private final ImageService imageService;
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenUtil jwtTokenUtil;

  /*
  [회원가입]
  Request: image 파일, uid, password, name, phone
  Response: user PK, profileImgUrl, uid, name, roles
   */
  @Override
  public SignUpResponse signUp(MultipartFile image, SignUpRequest request) {

    log.info("[signUp] 회원가입 요청 - uid : {}", request.getUid());
    // 이미 존재하는 유저일때
    if (userRepository.existsByUidAndDeletedAtNull(request.getUid())) {
      throw new BaseException(ALREADY_REGISTERED_USER);
    }
    // 유저 이름이 중복될때
    if (userRepository.existsByNameAndDeletedAtNull(request.getName())) {
      throw new BaseException(DUPLICATED_USER_NAME);
    }

    // 파일 저장 후 url 가져오기
    String imageUrl = imageService.saveImage(image, FileFolder.PROFILE_IMAGE);

    // User -> UserDto -> SignUpResponse form 가공
    return SignUpResponse.from(
        UserDto.from(userRepository.save(User.builder()
                .uid(request.getUid())  // 아이디
                .password(passwordEncoder.encode(request.getPassword())) // 비밀번호 암호화 저장
                .profileImgUrl(imageUrl)  // image Url 저장 (http://...)
                .name(request.getName())  // 유저 이름
                .phone(request.getPhone()) // 유저 핸드포 번호
                .roles(Collections.singletonList("ROLE_VIEWER")) // 기본 권한 VIEWER
                .build()
            )
        )
    );
  }

  /*
  [로그인]
  Request: uid, password
  Response: user PK, uid, role List, accessToken
   */
  @Override
  @Transactional
  public SignInResponse signIn(SignInRequest request, HttpServletResponse response) {
    log.info("[signIn] 로그인 - uid : {}", request.getUid());

    // user 정보 추출
    User user = userRepository.findByUidAndDeletedAtNull(request.getUid())
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    log.info("[signIn] 패스워드 일치 여부 확인");
    // 비밀번호 일치 여부 확인
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BaseException(UN_MATCH_PASSWORD);
    }
    log.info("[signIn] 패스워드 일치 여부 확인 완료");

    String accessToken = jwtTokenUtil.createAccessToken(user);
    String refreshToken = jwtTokenUtil.createRefreshToken(user);
    // Token 생성
    Token token = Token.builder()
        .uid(user.getUid())
        .refreshToken(refreshToken)
        .accessToken(accessToken)
        .expiredTime(jwtTokenUtil.getExpiration(refreshToken))
        .build();

    // accessToken, refreshToken Redis 에 저장
    tokenService.saveTokenInfo(token);

    return SignInResponse.from(
        UserDto.from(user), token
    );

  }

  /*
  [로그아웃]
  Request: Header 의 accessToken
   */
  @Override
  @Transactional
  public void logout(HttpServletRequest request) {
    log.info("[logout] 로그아웃");

    // accessToken 추출
    String accessToken = jwtTokenUtil.resolveToken(request);
    // 토큰 남은 만료시간
    Long expiredTime = jwtTokenUtil.getExpiration(accessToken);

    Token token = tokenRepository.findByAccessToken(accessToken)
        .orElseThrow(() -> new BaseException(NOT_FOUND_TOKEN));

    // 이미 로그아웃 된 상태
    if (token.getRefreshToken().equals("LOGOUT")) {
      throw new BaseException(ALREADY_LOGOUT);
    }

    token.setRefreshToken("LOGOUT");
    token.setExpiredTime(expiredTime);
    tokenService.saveTokenInfo(token);
  }

  /*
  [토큰 재발급]
  Request: HttpServletRequest
   */
  @Override
  @Transactional
  public ReissueResponse reissueToken(HttpServletRequest request) {
    log.info("[reissueToken] 토큰 재발급 시작");

    // accessToken 으로 Redis 에 저장되어있는 Token 정보 추출
    Token token = tokenRepository
        .findByAccessToken(jwtTokenUtil.resolveToken(request))
        .orElseThrow(() -> new BaseException(NOT_FOUND_TOKEN));

    // refreshToken
    String refreshToken = token.getRefreshToken();

    // refreshToken verify check
    if (refreshToken == null || !refreshToken.equals(request.getHeader("refreshToken"))) {
      throw new BaseException(INVALID_TOKEN);
    }
    // refreshToken 이 만료되었을때
    try {
      jwtTokenUtil.validateToken(refreshToken);
    } catch (ExpiredJwtException e) {
      throw new BaseException(REFRESH_TOKEN_EXPIRED_TOKEN);
    }

    User user = userRepository.findByUid(token.getUid())
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    // 새로운 accessToken 생성
    String newAccessToken = jwtTokenUtil.createAccessToken(user);

    // 새로운 accessToken 으로 교체
    token.setAccessToken(newAccessToken);
    tokenService.saveTokenInfo(token);

    return ReissueResponse.from(token);
  }

}
