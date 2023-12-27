package org.jnjeaaaat.onbition.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 유저 정보 변경 Response class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModifyResponse {

  private Long id;
  private String uid;
  private String name;
  private String profileImgUrl;

  public static UserModifyResponse from(UserDto userDto) {
    return UserModifyResponse.builder()
        .id(userDto.getId())
        .uid(userDto.getUid())
        .name(userDto.getName())
        .profileImgUrl(userDto.getProfileImgUrl())
        .build();
  }

}
