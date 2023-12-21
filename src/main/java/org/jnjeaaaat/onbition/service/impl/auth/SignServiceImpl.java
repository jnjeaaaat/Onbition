package org.jnjeaaaat.onbition.service.impl.auth;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.ALREADY_REGISTERED_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.DUPLICATED_USER_NAME;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_PASSWORD;

import java.util.Collections;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.file.FileFolder;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInResponse;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.entity.token.AccessToken;
import org.jnjeaaaat.onbition.domain.entity.token.RefreshToken;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.ImageService;
import org.jnjeaaaat.onbition.service.SignService;
import org.jnjeaaaat.onbition.service.TokenService;
import org.jnjeaaaat.onbition.util.JwtTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

    // AccessToken 생성
    AccessToken accessToken = jwtTokenUtil.createAccessToken(user);
    // RefreshToken 생성
    RefreshToken refreshToken = jwtTokenUtil.createRefreshToken(user);

    // Header 에 accessToken 저장
    jwtTokenUtil.setHeaderAccessToken(response, accessToken.getToken());

    // accessToken, refreshToken Redis 에 저장
    tokenService.saveTokenInfo(accessToken);
    tokenService.saveTokenInfo(refreshToken);

    return SignInResponse.from(
        UserDto.from(user), accessToken.getToken()
    );

  }

}
