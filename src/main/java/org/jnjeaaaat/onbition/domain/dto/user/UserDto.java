package org.jnjeaaaat.onbition.domain.dto.user;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.entity.User;

/**
 * User entity 클래스를
 * 사용자에 가까운 UserDto 로 가공한 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

  private Long id;        // 유저 pk
  private String uid;     // 유저 id
  private String password;  // 유저 비밀번호
  private String profileImgUrl; // 유저 프로필 사진
  private String name;      // 유저 이름
  private String phone;     // 유저 핸드폰번호
  private String cardNum;   // 유저 카드번호
  private String accountNum;  // 유저 계좌번호
  private boolean isDeleted;  // 유저 삭제 여부
  private List<String> roles; // 유저 권한

  private LocalDateTime createdAt;  // 유저 생성 일자
  private LocalDateTime updatedAt;  // 유저정보 변경 일자
  private LocalDateTime deletedAt;  // 유저 삭제 일자

  public static UserDto from(User user) {
    return UserDto.builder()
        .id(user.getId())   // 유저 pk
        .uid(user.getUid()) // 유저 id
        .password(user.getPassword())   // 유저 비밀번호
        .profileImgUrl(user.getProfileImgUrl()) // 유저 프로필 사진
        .name(user.getName())   // 유저 이름
        .phone(user.getPhone()) // 유저 핸드폰 번호
        .cardNum(user.getCardNum()) // 유저 카드번호
        .accountNum(user.getAccountNum()) // 유저 계좌번호
        .isDeleted(user.isDeleted())  // 유저 삭제 여부
        .roles(user.getRoles())   // 유저 권한 목록

        .createdAt(user.getCreatedAt())   // 유저 생성 일자
        .updatedAt(user.getUpdatedAt())   // 유저정보 변경 일자
        .deletedAt(user.getDeletedAt())   // 유저 삭제 일자
        .build();
  }
}
