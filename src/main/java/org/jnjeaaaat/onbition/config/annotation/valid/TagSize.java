package org.jnjeaaaat.onbition.config.annotation.valid;

import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 그림 태그 최대 사이즈 validation annotation
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TagSizeValidator.class)
public @interface TagSize {

  String message() default "태그는 10개까지만 입력할 수 있습니다.";

  Class[] groups() default {};

  Class[] payload() default {};

}
