package org.jnjeaaaat.onbition.domain.dto.sign;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;

/**
 * 로그인 후 사용자가 받을 Response Dto Class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResponse {

  private Long id;  // 유저 PK
  private String uid;   // 유저 id
  private List<String> roles;  // 유저 권한
  private String accessToken;  // accessToken

  public static SignInResponse from(UserDto userDto, String accessToken) {
    return SignInResponse.builder()
        .id(userDto.getId())
        .uid(userDto.getUid())
        .roles(userDto.getRoles())
        .accessToken(accessToken)
        .build();
  }

}
