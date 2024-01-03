package org.jnjeaaaat.onbition.domain.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserDetails interface 구현체 class
 */
@Getter
@Setter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private final User user;

  /*
  계정이 가지고 있는 권한 목록 리턴
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return user.getRoles()
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toSet());
  }

  /*
  계정의 비밀번호를 리턴
   */
  @Override
  public String getPassword() {
    return user.getPassword();
  }

  /*
  계정의 이름을 리턴
  일반적으로 아이디를 리턴
   */
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public String getUsername() {
    return user.getUid();
  }

  /*
  계정이 만료되었는지를 리턴
  true 는 만료되지 않았음을 의미
   */
  @JsonProperty(access = Access.WRITE_ONLY)
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /*
  계정이 잠겨있는지 리턴
  true 는 잠겨있지 않음을 의미
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
