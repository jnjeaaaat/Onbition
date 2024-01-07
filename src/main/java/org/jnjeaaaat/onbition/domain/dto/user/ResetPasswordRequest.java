package org.jnjeaaaat.onbition.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jnjeaaaat.onbition.config.annotation.valid.Telephone;

/**
 * 비밀번호 초기화 Request class
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {

  @NotBlank(message = "아이디를 입력해주세요.")
  private String uid;

  @Telephone
  private String phone;

}
