package org.jnjeaaaat.onbition.domain.dto.sign;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jnjeaaaat.onbition.config.annotation.valid.Telephone;

/**
 * 회원가입에 필요한 Request class
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

  @NotBlank(message = "아이디를 입력해주세요.")
  private String uid;

  //TODO: 비밀번호 pattern validation 필요
  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;

  @NotBlank(message = "이름을 입력해주세요.")
  private String name;

  @Telephone
  private String phone;

}
