package org.jnjeaaaat.onbition.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * jwt 토큰으로부터 유저 정보를 받아오기 위한 annotation
 */
@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUser {

  boolean errorOnInvalidType() default false;

  String expression() default "";

}
