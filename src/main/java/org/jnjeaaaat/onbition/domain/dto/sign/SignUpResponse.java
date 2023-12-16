package org.jnjeaaaat.onbition.domain.dto.sign;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.dto.user.UserDto;

/**
 * 회원가입 후 클라이언트가 확인할 수 있는 Response class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponse {

  private Long id;  // 유저 PK
  private String profileImgUrl;  // 유저 프로필사진 Url
  private String uid;   // 유저 id
  private String name;  // 유저 이름
  private List<String> roles;   // 유저 권한 목록

  public static SignUpResponse from(UserDto userDto) {
    return SignUpResponse.builder()
        .id(userDto.getId())
        .profileImgUrl((userDto.getProfileImgUrl()))
        .uid(userDto.getUid())
        .name(userDto.getName())
        .roles(userDto.getRoles())
        .build();
  }

}
