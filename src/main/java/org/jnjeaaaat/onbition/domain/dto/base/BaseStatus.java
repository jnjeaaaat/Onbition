package org.jnjeaaaat.onbition.domain.dto.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.springframework.http.HttpStatus.*;

/**
 * Response 할 때 전달하는 상태값
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum BaseStatus {

  /////////////////////////////// success response ///////////////////////////////
  // common
  SUCCESS(true, OK.value(), "성공했습니다."),

  // user
  SUCCESS_SIGN_UP(true, OK.value(), "성공적으로 회원가입 되었습니다."),



  //////////////////////////////// failed response ////////////////////////////////
  // user
  ALREADY_REGISTERED_USER(false, BAD_REQUEST.value(), "이미 가입된 유저입니다."),
  DUPLICATED_USER_NAME(false, BAD_REQUEST.value(), "중복된 이름입니다."),
  ;

  private boolean success;
  private int code;
  private String message;
}
