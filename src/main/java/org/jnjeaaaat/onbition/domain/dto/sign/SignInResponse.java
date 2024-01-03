package org.jnjeaaaat.onbition.domain.dto.sign;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;
import org.jnjeaaaat.onbition.domain.entity.token.Token;

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
  private Set<String> roles;  // 유저 권한
  private String accessToken;  // accessToken
  private String refreshToken; // refreshToken

  public static SignInResponse from(UserDto userDto, Token token) {
    return SignInResponse.builder()
        .id(userDto.getId())
        .uid(userDto.getUid())
        .roles(userDto.getRoles())
        .accessToken(token.getAccessToken())
        .refreshToken(token.getRefreshToken())
        .build();
  }

}
