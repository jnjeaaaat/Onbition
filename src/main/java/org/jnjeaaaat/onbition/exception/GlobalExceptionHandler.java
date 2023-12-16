package org.jnjeaaaat.onbition.exception;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ExceptionHandler BaseException(customException), RuntimeException, MethodException handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /*
  [BaseExceptionHandler]
  BaseException 발생 시
  BaseResponse 객체로 반환
   */
  @ExceptionHandler(BaseException.class)
  public ResponseEntity<BaseResponse> handleBaseException(
      BaseException e, HttpServletRequest request) {
    log.error("[BaseException] {} is occurred. uri:{}", e.getStatus(), request.getRequestURI());

    return ResponseEntity
        .badRequest()
        .body(BaseResponse.fail(e.getStatus()));
  }

}
