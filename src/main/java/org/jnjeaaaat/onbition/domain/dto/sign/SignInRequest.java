package org.jnjeaaaat.onbition.domain.dto.sign;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인에 필요한 사용자 입력 Request Class
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

  @NotBlank(message = "아이디를 입력해주세요.")
  private String uid;

  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;

}
