package org.jnjeaaaat.onbition.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

/**
 * Telephone Validation 처리를 위한 Custom Annotation
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TelephoneValidator.class)
public @interface Telephone {

  String message() default "전화번호 형식이 일치하지 않습니다.";

  Class[] groups() default {};

  Class[] payload() default {};

}
