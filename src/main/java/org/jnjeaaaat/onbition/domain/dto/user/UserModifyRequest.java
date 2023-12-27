package org.jnjeaaaat.onbition.domain.dto.user;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 정보 변경 Request class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModifyRequest {

  @NotBlank(message = "이름을 입력해주세요.")
  private String name;

}
