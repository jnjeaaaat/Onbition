package org.jnjeaaaat.onbition.config.filter;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NO_AUTHORITY;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.filter.dto.EntryPointErrorResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 권한이 없는 유저가 접근했을 때 발생하는 ExceptionHandling Class
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("[handle] 접근이 막혔을 경우 에러 메세지 리턴");

        EntryPointErrorResponse entryPointErrorResponse = new EntryPointErrorResponse(NO_AUTHORITY);

        response.setStatus(entryPointErrorResponse.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(entryPointErrorResponse));
    }
}
