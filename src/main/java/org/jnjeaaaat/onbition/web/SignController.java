package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_LOG_OUT;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_REISSUE_TOKEN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_SIGN_IN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_SIGN_UP;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.security.JwtTokenProvider;
import org.jnjeaaaat.onbition.domain.dto.auth.ReissueResponse;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInResponse;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.jnjeaaaat.onbition.service.SignService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 회원가입, 로그인 api
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SignController {

  private final SignService signService;
  private final JwtTokenProvider jwtTokenProvider;

  /*
  [회원가입]
  Request: image 파일, uid, password, name, phone
  Response: user PK, profileImgUrl, uid, name, roles
   */
  @PostMapping(value = "/sign-up", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  // Multipart, RequestPart 에 Json 형태의 값을 받기 위해 MediaType 을 설정해줘야 한다.
  public BaseResponse<SignUpResponse> signUp(
      @RequestPart(value = "image", required = false) MultipartFile image,
      // MultipartFile 이미지 request
      @Valid @RequestPart(value = "request") SignUpRequest request, // 나머지 유저 정보 request
      HttpServletRequest httpServletRequest) throws IOException { // uri 을 받기 위한 servletRequest

    // 요청 uri log
    log.info("[signUp] 회원가입 요청 - uri : {}", httpServletRequest.getRequestURI());

    return BaseResponse.success(SUCCESS_SIGN_UP, signService.signUp(image, request));

  }

  /*
  [로그인]
  Request: uid, password
  Response: user PK, uid, role List, accessToken
   */
  @PostMapping(value = "/sign-in")
  public BaseResponse<SignInResponse> signIn(
      @Valid @RequestBody SignInRequest request,
      HttpServletRequest httpServletRequest) {

    // 요청 uri log
    log.info("[signIn] 로그인 요청 - uri : {}", httpServletRequest.getRequestURI());

    return BaseResponse.success(
        SUCCESS_SIGN_IN,
        signService.signIn(request)
    );
  }

  @PostMapping("/logout")
  public BaseResponse logout(HttpServletRequest request) {

    log.info("[logout] 로그아웃 요청");

    signService.logout(request);
    return BaseResponse.success(
        SUCCESS_LOG_OUT
    );

  }

  /*
  [토큰 재발급]
  Request: HttpServletRequest
   */
  @PostMapping("/re-token")
  public BaseResponse<ReissueResponse> reissueToken(HttpServletRequest httpServletRequest) {

    log.info("[reIssueToken] 토큰 재발급 요청");

    return BaseResponse.success(
        SUCCESS_REISSUE_TOKEN,
        signService.reissueToken(httpServletRequest)
    );


  }

  /*
  token 테스트를 위한 api
   */
  @GetMapping("/test")
  public BaseResponse test(HttpServletRequest request) {
    log.info("[test] token : {}", jwtTokenProvider.resolveToken(request));

    String token = jwtTokenProvider.resolveToken(request);
    log.info("[test] isExpiredToken: {}", jwtTokenProvider.validateToken(token));

    Long userId = jwtTokenProvider.getUserIdFromToken();
    log.info("[test] userId : {}", userId);


    return BaseResponse.success(
        SUCCESS
    );
  }

}
