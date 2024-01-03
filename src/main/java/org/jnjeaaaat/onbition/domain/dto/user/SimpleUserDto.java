package org.jnjeaaaat.onbition.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.entity.User;

/**
 * 다른 Entity return 값에 포함할 간단한 User 정보 class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleUserDto {

  private Long userId;
  private String profileImgUrl;
  private String uid;
  private String name;
  private String phone;

  public static SimpleUserDto from(User user) {
    return SimpleUserDto.builder()
        .userId(user.getId())
        .profileImgUrl(user.getProfileImgUrl())
        .uid(user.getUid())
        .name(user.getName())
        .phone(user.getPhone())
        .build();
  }

}
