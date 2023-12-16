package org.jnjeaaaat.onbition.service;

import org.jnjeaaaat.onbition.domain.dto.auth.SendTextResponse;

/**
 * 문자전송, 인증 Service interface
 */
public interface SMSService {

  // 문자 전송
  SendTextResponse sendMessage(String phone);

  // 인증 코드 비교
  String authenticatePhoneNum(String phone, String code);

}
