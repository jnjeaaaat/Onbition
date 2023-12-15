package org.jnjeaaaat.onbition.config.security;

import java.util.Arrays;
import java.util.List;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Entity 에 List<String> 타입의 데이터를 JSON 형태로 저장하기 위한 Converter
 */
@Converter
public class StringConverter implements AttributeConverter<List<String>, String> {
  private static final String SPLIT_CHAR = ";";

  @Override
  public String convertToDatabaseColumn(List<String> stringList) {
    return String.join(SPLIT_CHAR, stringList);
  }

  @Override
  public List<String> convertToEntityAttribute(String string) {
    return Arrays.asList(string.split(SPLIT_CHAR));
  }
}
