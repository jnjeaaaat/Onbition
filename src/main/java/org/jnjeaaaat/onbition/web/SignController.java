package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_SIGN_UP;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.jnjeaaaat.onbition.service.SignService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
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

  /*
  [회원가입]
  Request: image 파일, uid, password, name, phone
  Response: user PK, profileImgUrl, uid, name, roles
   */
  @PostMapping(value = "/sign-up", consumes = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.MULTIPART_FORM_DATA_VALUE})
  // Multipart, RequestPart 에 Json 형태의 값을 받기 위해 MediaType 을 설정해줘야 한다.
  public BaseResponse<SignUpResponse> signUp(
      @RequestPart(value = "image", required = false) MultipartFile image, // MultipartFile 이미지 request
      @Valid @RequestPart(value = "request") SignUpRequest request, // 나머지 유저 정보 request
      HttpServletRequest httpServletRequest ) { // uri 을 받기 위한 servletRequest

    // 요청 uri log
    log.info("[signUp] 회원가입 요청 - uri : {}", httpServletRequest.getRequestURI());

    return BaseResponse.success(SUCCESS_SIGN_UP, signService.signUp(image, request));

  }

}
