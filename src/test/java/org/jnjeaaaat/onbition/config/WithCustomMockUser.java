package org.jnjeaaaat.onbition.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * token 권한이 필요한 test method 에 적용하는 Annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {

  String uid() default "testId";

  String role() default "ROLE_VIEWER";

}
