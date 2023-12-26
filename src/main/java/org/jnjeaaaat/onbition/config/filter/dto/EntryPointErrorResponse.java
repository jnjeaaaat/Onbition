package org.jnjeaaaat.onbition.config.filter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jnjeaaaat.onbition.domain.dto.base.BaseStatus;

/**
 * Filter 에서 발생하는 예외 Response Dto Class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EntryPointErrorResponse {

  private boolean success;    // 성공 여부 true, false
  private int code;           // HttpStatus 200, 400, ...
  private String message;     // 성공 메세지, 에러 메세지

  public EntryPointErrorResponse(BaseStatus status) {
    this.success = status.isSuccess();
    this.code = status.getCode();
    this.message = status.getMessage();
  }
}
