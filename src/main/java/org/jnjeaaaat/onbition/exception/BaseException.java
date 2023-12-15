package org.jnjeaaaat.onbition.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jnjeaaaat.onbition.domain.dto.base.BaseStatus;

/**
 * RuntimeException 을 상속하는 CustomException 성공여부, http code, message 값 반환
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends RuntimeException {

  private BaseStatus status;
  private boolean success;  // 성공 여부
  private int code;         // http status
  private String message;   // message

  // BaseException Handler 로 넘겨주기 위한 생성자
  public BaseException(BaseStatus status) {
    this.status = status;
    this.success = status.isSuccess();
    this.code = status.getCode();
    this.message = status.getMessage();
  }

}
