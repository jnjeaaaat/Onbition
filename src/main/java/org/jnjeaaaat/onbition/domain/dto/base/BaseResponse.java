package org.jnjeaaaat.onbition.domain.dto.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseResponse<T> {

  private boolean success;
  private int code;
  private String message;
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T result;

  public static <T> BaseResponse<T> success(BaseStatus status, T result) {
    return BaseResponse.<T>builder()
        .success(status.isSuccess())
        .code(status.getCode())
        .message(status.getMessage())
        .result(result)
        .build();
  }

  public static <T> BaseResponse<T> fail(BaseStatus status) {
    return BaseResponse.<T>builder()
        .success(status.isSuccess())
        .code(status.getCode())
        .message(status.getMessage())
        .build();
  }

}
