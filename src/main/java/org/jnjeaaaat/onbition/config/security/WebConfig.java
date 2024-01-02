package org.jnjeaaaat.onbition.config.security;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jnjeaaaat.onbition.config.annotation.AuthUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * custom HandlerMethodArgumentResolver 등록을 위한 WebConfig class
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final AuthUserArgumentResolver authUserArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(authUserArgumentResolver);
  }
}
