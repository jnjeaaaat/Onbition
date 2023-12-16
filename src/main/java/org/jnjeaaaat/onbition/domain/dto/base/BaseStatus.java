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

  // sms
  SUCCESS_SEND_TEXT(true, OK.value(), "문자를 전송하였습니다."),
  SUCCESS_AUTH_TEXT(true, OK.value(), "문자인증을 완료하였습니다."),

  SUCCESS_SIGN_UP(true, OK.value(), "성공적으로 회원가입 되었습니다."),



  //////////////////////////////// failed response ////////////////////////////////
  // user
  ALREADY_REGISTERED_USER(false, BAD_REQUEST.value(), "이미 가입된 유저입니다."),

  // sms auth
  NEED_REPOST_PHONE_NUMBER(false, BAD_REQUEST.value(), "번호를 다시 입력해주세요."),
  UN_MATCH_VERIFICATION_CODE(false, BAD_REQUEST.value(), "인증번호가 일치하지 않습니다."),
  ;

  private boolean success;
  private int code;
  private String message;
}
