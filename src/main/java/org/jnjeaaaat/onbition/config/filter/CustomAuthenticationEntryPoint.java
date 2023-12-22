package org.jnjeaaaat.onbition.config.filter;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.EXPIRED_TOKEN;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.INVALID_TOKEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.filter.dto.EntryPointErrorResponse;
import org.jnjeaaaat.onbition.domain.dto.base.BaseStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * 토큰 유효성에 문제가 있을때 예외처리하는 ExceptionHandling Class
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    // Filter 로 부터 넘어온 request 에서 getAttribute()로 발생한 예외 추출
    Object exception = request.getAttribute("exception");

    log.info("[commence] 인증 실패로 response.sendError 발생");

    // responseForm에 저장
    EntryPointErrorResponse entryPointErrorResponse = new EntryPointErrorResponse();

    if (exception == EXPIRED_TOKEN) {
      log.info("[commence] 토큰 만료 에러 발생");
      entryPointErrorResponse = new EntryPointErrorResponse(EXPIRED_TOKEN);
    } else if (exception == INVALID_TOKEN) {
      log.info("[commence] 유효하지 않는 토큰 에러 발생");
      entryPointErrorResponse = new EntryPointErrorResponse(INVALID_TOKEN);
    }

    response.setStatus(entryPointErrorResponse.getCode());
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
  }

}
