package org.jnjeaaaat.onbition.service;

import javax.servlet.http.HttpServletResponse;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignInResponse;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpRequest;
import org.jnjeaaaat.onbition.domain.dto.sign.SignUpResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 유저 회원가입, 로그인 service interface
 */
public interface SignService {

  // 회원가입
  SignUpResponse signUp(MultipartFile image, SignUpRequest request);

  // 로그인
  SignInResponse signIn(SignInRequest request, HttpServletResponse response);

}