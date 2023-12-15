package org.jnjeaaaat.onbition.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.config.security.StringConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * SpringSecurity UserDetails 구현하는 User Entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;    // 유저 PK

  @Column(unique = true)
  private String uid; // 유저 id

  @JsonProperty(access = Access.WRITE_ONLY)
  @Column(nullable = false)
  private String password; // 유저 비밀번호

  @Column(nullable = false)
  private String profileImgUrl; // 유저 프로필사진 url

  @Column(unique = true)
  private String name;  // 유저 이름

  @Column(nullable = false)
  private String phone; // 유저 핸드폰번호

  @Column
  private String cardNum; // 유저 카드번호

  @Column
  private String accountNum; // 유저 계좌번호

  @Column
  private boolean isDeleted;  // 유저 삭제 여부

  @Column
  private LocalDateTime deletedAt;  // 유저 삭제 일자

  @Convert(converter = StringConverter.class)
  @Builder.Default
  private List<String> roles = new ArrayList<>(); // 유저 권한 리스트

  /*
   계정이 가지고 있는 권한 목록 리턴
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }


  /*
  계정의 이름을 리턴
  일반적으로 아이디(이메일)를 리턴
   */
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public String getUsername() {
    return this.uid;
  }

  /*
  계정이 만료되었는지를 리턴
  true는 만료되지 않았음을 의미
   */
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /*
  계정이 잠겨있는지 리턴
  true는 잠겨있지 않음을 의미
   */
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /*
  비밀번호가 만료되었는지 리턴
   */
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /*
  계정이 활성화되어 있는지 리턴
   */
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isEnabled() {
    return true;
  }
}
