package org.jnjeaaaat.onbition.domain.dto.base;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  // auth
  SUCCESS_SIGN_UP(true, OK.value(), "성공적으로 회원가입 되었습니다."),
  SUCCESS_SIGN_IN(true, OK.value(), "로그인 되었습니다."),
  SUCCESS_REISSUE_TOKEN(true, OK.value(), "토큰이 재발급 되었습니다."),
  SUCCESS_LOG_OUT(true, OK.value(), "로그아웃 되었습니다."),

  // user
  SUCCESS_UPDATE_USER(true, OK.value(), "유저 정보가 변경되었습니다."),
  SUCCESS_UPDATE_PASSWORD(true, OK.value(), "비밀번호가 변경되었습니다."),
  SUCCESS_RESET_PASSWORD(true, OK.value(), "비밀번호가 초기화 되었습니다."),

  // painting
  SUCCESS_CREATE_PAINTING(true, OK.value(), "그림을 등록하였습니다."),
  SUCCESS_SEARCH_PAINTING(true, OK.value(), "그림을 조회하였습니다."),
  SUCCESS_SEARCH_PAINTINGS_BY_TITLE(true, OK.value(), "제목으로 그림을 검색하였습니다."),
  SUCCESS_MODIFY_PAINTING_TAGS(true, OK.value(), "태그를 수정하였습니다."),
  SUCCESS_CONVERT_TO_SALE(true, OK.value(), "그림 판매를 시작합니다."),
  SUCCESS_BUY_PAINTING(true, OK.value(), "그림을 구매하였습니다."),

  // money
  SUCCESS_CHANGE_MONEY(true, OK.value(), "잔액이 변경되었습니다."),



  //////////////////////////////// failed response ////////////////////////////////
  // user
  ALREADY_REGISTERED_USER(false, BAD_REQUEST.value(), "이미 가입된 유저입니다."),
  DUPLICATED_USER_NAME(false, BAD_REQUEST.value(), "중복된 이름입니다."),
  NOT_FOUND_USER(false, BAD_REQUEST.value(), "등록되지 않은 유저입니다."),
  UN_MATCH_PASSWORD(false, BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."),
  SAME_PASSWORD(false, BAD_REQUEST.value(), "동일한 비밀번호로는 변경할 수 없습니다."),
  UN_MATCH_PHONE_NUM(false, BAD_REQUEST.value(), "전화번호가 일치하지 않습니다."),

  // sms auth
  NEED_REPOST_PHONE_NUMBER(false, BAD_REQUEST.value(), "번호를 다시 입력해주세요."),
  UN_MATCH_VERIFICATION_CODE(false, BAD_REQUEST.value(), "인증번호가 일치하지 않습니다."),

  // auth
  ACCESS_TOKEN_EXPIRED_TOKEN(false, UNAUTHORIZED.value(), "만료된 토큰입니다. 재발급 요청해주세요."),
  REFRESH_TOKEN_EXPIRED_TOKEN(false, UNAUTHORIZED.value(), "다시 로그인해주세요"),
  NO_AUTHORITY(false, UNAUTHORIZED.value(), "권한이 없는 유저입니다."),
  NOT_FOUND_TOKEN(false, UNAUTHORIZED.value(), "토큰이 존재하지 않습니다."),
  INVALID_TOKEN(false, UNAUTHORIZED.value(), "유효하지 않는 토큰입니다."),
  ALREADY_LOGOUT(false, UNAUTHORIZED.value(), "이미 로그아웃 상태입니다."),

  // painting
  UNDER_MIN_PRICE(false, BAD_REQUEST.value(), "판매되는 그림의 가격은 1000원 이상이어야 합니다."),
  NOT_FOUND_PAINTING(false, BAD_REQUEST.value(), "해당 그림이 없습니다."),
  UN_MATCH_USER(false, BAD_REQUEST.value(), "그림의 소유자만 변경할 수 있습니다."),
  ALREADY_OWNED_PAINTING(false, BAD_REQUEST.value(), "이미 그림의 소유주입니다."),
  NOT_SALE_PAINTING(false, BAD_REQUEST.value(), "판매중인 그림이 아닙니다."),

  // painting elasticsearch
  WRONG_PRICE_RANGE(false, BAD_REQUEST.value(), "최소금액이 최대금액보다 클 수 없습니다."),

  // money
  AMOUNT_LESS_THAN_CURRENT(false, BAD_REQUEST.value(), "잔액이 부족합니다."),

  // file
  NOT_FOUND_FILE(false, INTERNAL_SERVER_ERROR.value(), "파일을 찾을 수 없습니다."),
  

  ;

  private boolean success;
  private int code;
  private String message;
}
