package org.jnjeaaaat.onbition.config.annotation.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.jnjeaaaat.onbition.config.annotation.valid.Telephone;

/**
 * @Telephone 어노테이션 구현체
 */
public class TelephoneValidator implements ConstraintValidator<Telephone, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    return value.matches("01(?:0|1|[6-9])-(\\d{3}|\\d{4})-(\\d{4})$");
  }

}
