package org.jnjeaaaat.onbition.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 비밀번호 변경 Request class
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordModifyRequest {

  @NotBlank(message = "비밀번호를 입력해주세요.")
  private String password;

  @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
  private String newPassword;

}
