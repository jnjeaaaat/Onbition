package org.jnjeaaaat.onbition.config.annotation;

import java.lang.annotation.Annotation;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * AuthUser annotation 구현체
 */
@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return findMethodAnnotation(AuthUser.class, parameter) != null;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return null;
    }
    Object principal = authentication.getPrincipal();
    if (principal != null && !parameter.getParameterType().isAssignableFrom(principal.getClass())) {
      AuthUser authPrincipal = findMethodAnnotation(AuthUser.class, parameter);
      if (authPrincipal.errorOnInvalidType()) {
        throw new ClassCastException(
            principal + " is not assignable to " + parameter.getParameterType());
      }
      return null;
    }
    return principal;
  }

  private <T extends Annotation> T findMethodAnnotation(Class<T> annotationClass,
      MethodParameter parameter) {
    T annotation = parameter.getParameterAnnotation(annotationClass);
    if (annotation != null) {
      return annotation;
    }
    Annotation[] annotationsToSearch = parameter.getParameterAnnotations();
    for (Annotation toSearch : annotationsToSearch) {
      annotation = AnnotationUtils.findAnnotation(toSearch.annotationType(), annotationClass);
      if (annotation != null) {
        return annotation;
      }
    }
    return null;
  }
}
