package org.jnjeaaaat.onbition.domain.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 클라이언트가 확인할 수 있는 ResponseDto
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

  private boolean success;    // 성공 여부 true, false
  private int code;           // HttpStatus 200, 400, ...
  private String message;     // 성공 메세지, 에러 메세지
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T result;           // 성공 했을때 결과값

  // 성공 케이스
  public static <T> BaseResponse<T> success(BaseStatus status, T result) {
    return BaseResponse.<T>builder()
        .success(status.isSuccess())
        .code(status.getCode())
        .message(status.getMessage())
        .result(result)
        .build();
  }

  // 실패 케이스 BaseException 을 처리함
  public static <T> BaseResponse<T> fail(BaseStatus status) {
    return BaseResponse.<T>builder()
        .success(status.isSuccess())
        .code(status.getCode())
        .message(status.getMessage())
        .build();
  }

  // 실패 케이스 MethodArgumentNotValidException 처리
  public static <T> BaseResponse<T> fail(HttpStatus status, String message) {
    return BaseResponse.<T>builder()
        .success(false)
        .code(status.value())
        .message(message)
        .build();
  }

}
