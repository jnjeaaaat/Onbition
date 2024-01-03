package org.jnjeaaaat.onbition.config.annotation.valid;

import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 그림 태그 최대사이즈 annotation 구현체 class
 */
public class TagSizeValidator implements ConstraintValidator<TagSize, Set<String>> {

  @Override
  public boolean isValid(Set<String> value, ConstraintValidatorContext context) {
    return value.size() <= 10;
  }

}
