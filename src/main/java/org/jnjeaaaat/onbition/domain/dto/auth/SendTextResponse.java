package org.jnjeaaaat.onbition.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 문자 전송 후 인증코드 return 을 위한 Response Class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendTextResponse {

  private String verificationCode;  // 인증 코드

}
